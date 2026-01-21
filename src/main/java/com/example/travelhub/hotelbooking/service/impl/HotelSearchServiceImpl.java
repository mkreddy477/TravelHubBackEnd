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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HotelSearchServiceImpl implements HotelSearchService {
    
    private final WebClient webClient;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public HotelSearchServiceImpl(
            ObjectMapper objectMapper,
            @Value("${hotel.api.search-url}") String baseUrl,
            @Value("${hotel.api.key}") String apiKey) {
    
        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> {
                configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024); // 10 MB
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
        this.objectMapper = objectMapper.copy()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Mono<HotelSearchResponse> search(HotelSearchRequest request) {
        log.info("Searching hotels for location: {}, check-in: {}, check-out: {}",
                request.getLocationId(),
                request.getCheckInDate(),
                request.getCheckOutDate());

        // Transform HotelSearchRequest to TripJackSearchRequest
        TripJackSearchRequest tripJackRequest = buildTripJackRequest(request);
        
        // Log the exact JSON being sent
        try {
            String requestJson = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(tripJackRequest);
            log.info("TripJack request JSON:\n{}", requestJson);
        } catch (JsonProcessingException e) {
            log.error("Error serializing request for logging", e);
        }

        return webClient.post()
                .uri("") // Empty string because full URL is in baseUrl
                .header("apikey", apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(tripJackRequest) // Send TripJackSearchRequest, not HotelSearchRequest
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
     * Convert HotelSearchRequest (frontend DTO) to TripJackSearchRequest (TripJack API format)
     */
    private TripJackSearchRequest buildTripJackRequest(HotelSearchRequest request) {
        log.debug("Building TripJack request from HotelSearchRequest");
        
        // Convert room info: HotelSearchRequest.RoomGuest -> TripJackSearchRequest.RoomInfo
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

        // Build hotel IDs list - locationId should contain hotel IDs from previous search
        List<String> hotelIds = new ArrayList<>();
        if (request.getLocationId() != null) {
            // TODO: This should be actual hotel IDs from location search results
            // For now using locationId as placeholder
            hotelIds.add(request.getLocationId());
            log.debug("Using location ID as hotel ID: {}", request.getLocationId());
        }

        // Build the TripJack request
        TripJackSearchRequest tripJackRequest = TripJackSearchRequest.builder()
                .searchQuery(TripJackSearchRequest.SearchQuery.builder()
                        .checkinDate(request.getCheckInDate().toString()) // LocalDate to "YYYY-MM-DD"
                        .checkoutDate(request.getCheckOutDate().toString()) // LocalDate to "YYYY-MM-DD"
                        .roomInfo(roomInfoList)
                        .searchCriteria(TripJackSearchRequest.SearchCriteria.builder()
                                .nationality("IN") // Make configurable if needed
                                .currency("INR")   // Make configurable if needed
                                .build())
                        .searchPreferences(TripJackSearchRequest.SearchPreferences.builder()
                                .ratings(ratings)
                                .hids(hotelIds) // Maximum 100 hotel IDs
                                .fsc(true)      // Free cancellation preference
                                .build())
                        .build())
                .sync(true)
                .build();

        log.debug("TripJack request built successfully");
        return tripJackRequest;
    }
}