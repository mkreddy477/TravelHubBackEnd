package com.example.travelhub.hotelbooking.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.travelhub.hotelbooking.models.TripJackSearchRequest;
import com.example.travelhub.hotelbooking.models.HotelSearchResponse;
import com.example.travelhub.hotelbooking.service.HotelSearchService;
import com.example.travelhub.locationservice.dto.HotelSearchRequest;
import com.example.travelhub.locationservice.service.LocationService;  // ✅ ADD THIS
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.travelhub.hotelbooking.service.HotelEnrichmentService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HotelSearchServiceImpl implements HotelSearchService {
    
    private final WebClient webClient;
    private final String apiKey;
    private final ObjectMapper objectMapper;
    private final LocationService locationService;  // ✅ ADD THIS
    private final HotelEnrichmentService enrichmentService;

    public HotelSearchServiceImpl(
            ObjectMapper objectMapper,
            LocationService locationService,
			HotelEnrichmentService enrichmentService,
            @Value("${hotel.api.search-url}") String baseUrl,
            @Value("${hotel.api.key}") String apiKey) {
    
        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> {
                configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024);
                configurer.defaultCodecs().jackson2JsonDecoder(
                    new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM)
                );
            })
            .build();
        
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .exchangeStrategies(strategies)
            .build();
        this.apiKey = apiKey;
        this.locationService = locationService;  // ✅ ADD THIS
        this.objectMapper = objectMapper.copy()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
	
	@Override
    public Mono<HotelSearchResponse> search(HotelSearchRequest request) {
        log.info("Searching hotels for location: {}, check-in: {}, check-out: {}",
                request.getLocationId(),
                request.getCheckInDate(),
                request.getCheckOutDate());

        return getHotelIds(request.getLocationId(), request.getMinRating())
                .flatMap(hotelIds -> {
                    if (hotelIds.isEmpty()) {
                        log.warn("No hotels found for location: {}", request.getLocationId());
                        return Mono.just(createEmptyResponse());
                    }

                    log.info("Found {} hotels for location {}", hotelIds.size(), request.getLocationId());
                    TripJackSearchRequest tripJackRequest = buildTripJackRequest(request, hotelIds);
                    
                    return callTripJackApi(tripJackRequest);
                })
                .map(response -> enrichmentService.enrichWithStaticData(response));  // ✅ ENRICH HERE
    }

    @Override
    public Mono<HotelSearchResponse> search(HotelSearchRequest request) {
        log.info("Searching hotels for location: {}, check-in: {}, check-out: {}",
                request.getLocationId(),
                request.getCheckInDate(),
                request.getCheckOutDate());

        // ✅ STEP 1: Get hotel IDs for the location
        return getHotelIds(request.getLocationId(), request.getMinRating())
                .flatMap(hotelIds -> {
                    if (hotelIds.isEmpty()) {
                        log.warn("No hotels found for location: {}", request.getLocationId());
                        return Mono.just(createEmptyResponse());
                    }

                    log.info("Found {} hotels for location {}", hotelIds.size(), request.getLocationId());

                    // ✅ STEP 2: Build TripJack request with actual hotel IDs
                    TripJackSearchRequest tripJackRequest = buildTripJackRequest(request, hotelIds);
                    
                    // Log the exact JSON being sent
                    try {
                        String requestJson = objectMapper.writerWithDefaultPrettyPrinter()
                                .writeValueAsString(tripJackRequest);
                        log.info("TripJack request JSON:\n{}", requestJson);
                    } catch (JsonProcessingException e) {
                        log.error("Error serializing request for logging", e);
                    }

                    // ✅ STEP 3: Call TripJack API
                    return callTripJackApi(tripJackRequest);
                });
    }

    /**
     * Get hotel IDs for a location
     */
    private Mono<List<String>> getHotelIds(String locationId, Integer minRating) {
        return Mono.fromCallable(() -> {
            List<String> hotelIds = locationService.getHotelIdsByLocation(locationId, minRating);
            
            // TripJack allows maximum 100 hotel IDs
            if (hotelIds.size() > 100) {
                log.warn("Location has {} hotels, limiting to 100 for TripJack API", hotelIds.size());
                return hotelIds.subList(0, 100);
            }
            
            return hotelIds;
        });
    }

    /**
     * Call TripJack API
     */
    private Mono<HotelSearchResponse> callTripJackApi(TripJackSearchRequest tripJackRequest) {
        return webClient.post()
                .uri("")
                .header("apikey", apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(tripJackRequest)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(rawResponse -> {
                    log.debug("Raw response (first 2000 chars): {}", 
                            rawResponse.substring(0, Math.min(2000, rawResponse.length())));
                })
                .flatMap(rawResponse -> {
                    try {
                        HotelSearchResponse response = objectMapper.readValue(rawResponse, HotelSearchResponse.class);
                        return Mono.just(response);
                    } catch (Exception e) {
                        log.error("Error parsing response: {}", e.getMessage(), e);
                        return Mono.error(e);
                    }
                })
                .doOnSuccess(response -> {
                    log.info("Successfully completed hotel search");
                    if (response.getSearchResult() != null) {
                        log.info("Total hotels found: {}", response.getSearchResult().getSize());
                        log.info("Hotels in response: {}", 
                            response.getSearchResult().getHis() != null 
                                ? response.getSearchResult().getHis().size() : 0);
                    } else {
                        log.warn("searchResult is NULL - check field name mapping!");
                    }
                })
                .doOnError(WebClientResponseException.class, error -> {
                    log.error("TripJack API error status: {}", error.getStatusCode());
                    log.error("TripJack API error response: {}", error.getResponseBodyAsString());
                })
                .doOnError(error -> log.error("Error during hotel search: {}", error.getMessage(), error));
    }

    /**
     * Convert HotelSearchRequest to TripJackSearchRequest with hotel IDs
     */
    private TripJackSearchRequest buildTripJackRequest(HotelSearchRequest request, List<String> hotelIds) {
        log.debug("Building TripJack request with {} hotel IDs", hotelIds.size());
        
        // Convert room info
        List<TripJackSearchRequest.RoomInfo> roomInfoList = request.getRooms().stream()
                .map(room -> {
                    log.debug("Converting room: adults={}, children={}", 
                            room.getAdults(), room.getChildren());
                    
                    return TripJackSearchRequest.RoomInfo.builder()
                            .numberOfAdults(room.getAdults() != null ? room.getAdults() : 2)
                            .numberOfChild(room.getChildren() != null ? room.getChildren() : 0)
                            .childAge(room.getChildAges() != null && !room.getChildAges().isEmpty() 
                                    ? room.getChildAges() 
                                    : new ArrayList<>())
                            .build();
                })
                .collect(Collectors.toList());

        // Validate: Maximum 5 rooms allowed by TripJack
        if (roomInfoList.size() > 5) {
            log.warn("TripJack allows maximum 5 rooms. Trimming from {} to 5 rooms", roomInfoList.size());
            roomInfoList = roomInfoList.subList(0, 5);
        }

        // Build ratings list
        List<Integer> ratings = new ArrayList<>();
        if (request.getMinRating() != null) {
            ratings.add(request.getMinRating());
            log.debug("Adding minimum rating filter: {}", request.getMinRating());
        }

        // Build the TripJack request with actual hotel IDs
        TripJackSearchRequest tripJackRequest = TripJackSearchRequest.builder()
                .searchQuery(TripJackSearchRequest.SearchQuery.builder()
                        .checkinDate(request.getCheckInDate().toString())
                        .checkoutDate(request.getCheckOutDate().toString())
                        .roomInfo(roomInfoList)
                        .searchCriteria(TripJackSearchRequest.SearchCriteria.builder()
                                .nationality("106")
                                .currency("INR")
                                .build())
                        .searchPreferences(TripJackSearchRequest.SearchPreferences.builder()
                                .ratings(ratings)
                                .hids(hotelIds)  // ✅ Use actual hotel IDs
                                .fsc(true)
                                .build())
                        .build())
                .sync(true)
                .build();

        log.debug("TripJack request built successfully with {} hotel IDs", hotelIds.size());
        return tripJackRequest;
    }

    /**
     * Create empty response when no hotels found
     */
    private HotelSearchResponse createEmptyResponse() {
        HotelSearchResponse response = new HotelSearchResponse();
        // Set appropriate empty values based on your HotelSearchResponse structure
        return response;
    }
}