package com.example.travelhub.hotelbooking.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.travelhub.hotelbooking.models.HotelSearchRequest;
import com.example.travelhub.hotelbooking.models.HotelSearchResponse;
import com.example.travelhub.hotelbooking.service.HotelSearchService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class HotelSearchServiceImpl implements HotelSearchService {

    private final WebClient webClient;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public HotelSearchServiceImpl(
            ObjectMapper objectMapper,
            @Value("${hotel.api.search-url}") String baseUrl,
            @Value("${hotel.api.key}") String apiKey) {
        
        // Configure Jackson decoder to accept application/octet-stream as JSON
        // (TripJack API returns octet-stream content type instead of application/json)
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
        // Create a copy of ObjectMapper configured to ignore unknown properties
        this.objectMapper = objectMapper.copy()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Mono<HotelSearchResponse> search(HotelSearchRequest request) {
        System.out.println("Initiating hotel search request");
        System.out.println("  - Check-in: " + (request.getSearchQuery() != null 
                ? request.getSearchQuery().getCheckinDate() : "N/A"));
        System.out.println("  - Check-out: " + (request.getSearchQuery() != null 
                ? request.getSearchQuery().getCheckoutDate() : "N/A"));
        System.out.println("  - Rooms: " + (request.getSearchQuery() != null 
                && request.getSearchQuery().getRoomInfo() != null 
                ? request.getSearchQuery().getRoomInfo().size() : 0));
        System.out.println("  - Sync: " + request.isSync());
        
        // Log the request being sent
        try {
            String requestJson = objectMapper.writeValueAsString(request);
            System.out.println("=== REQUEST TO TRIPJACK ===");
            System.out.println(requestJson);
            System.out.println("=== END REQUEST ===");
        } catch (Exception e) {
            System.err.println("Error serializing request: " + e.getMessage());
        }
        
        return webClient.post()
                .uri("/search")
                .header("apikey", apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(rawResponse -> {
                    System.out.println("=== RAW TRIPJACK HOTEL RESPONSE ===");
                    System.out.println(rawResponse.substring(0, Math.min(2000, rawResponse.length())));
                    System.out.println("=== END RAW RESPONSE ===");
                })
                .flatMap(rawResponse -> {
                    try {
                        HotelSearchResponse response = objectMapper.readValue(rawResponse, HotelSearchResponse.class);
                        return Mono.just(response);
                    } catch (Exception e) {
                        System.err.println("Error parsing response: " + e.getMessage());
                        return Mono.error(e);
                    }
                })
                .doOnSuccess(response -> {
                    System.out.println("Successfully completed hotel search");
                    if (response.getSearchResult() != null) {
                        System.out.println("  - Total hotels found: " + response.getSearchResult().getSize());
                        System.out.println("  - Hotels in response: " + 
                            (response.getSearchResult().getHis() != null 
                                ? response.getSearchResult().getHis().size() : 0));
                    } else {
                        System.out.println("  - searchResult is NULL - check field name mapping!");
                    }
                })
                .doOnError(error -> 
                    System.err.println("Error during hotel search: " + error.getMessage()));
    }
}