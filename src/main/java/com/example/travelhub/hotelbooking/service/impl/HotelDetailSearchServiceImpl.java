package com.example.travelhub.hotelbooking.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.travelhub.hotelbooking.models.HotelDetailResponse;
import com.example.travelhub.hotelbooking.models.HotelOperationRequest;
import com.example.travelhub.hotelbooking.service.HotelDetailSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class HotelDetailSearchServiceImpl implements HotelDetailSearchService {

    private final WebClient webClient;
    private final String apiKey;

    public HotelDetailSearchServiceImpl(
            ObjectMapper objectMapper,
            @Value("${hotel.api.search-detail-url}") String baseUrl,
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
    }

    @Override
    public Mono<HotelDetailResponse> getDetails(HotelOperationRequest request) {
        System.out.println("Fetching hotel details for Hotel ID: " + request.getId());
        
        return webClient.post()
                .uri("")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(HotelDetailResponse.class)
                .doOnSuccess(response -> {
                    System.out.println("Successfully fetched hotel details");
                    if (response.getHotel() != null) {
                      
                        System.out.println("  - Instructions: " + 
                            (response.getHotel().getInst() != null 
                                ? response.getHotel().getInst().size() : 0));
                    }
                })
                .doOnError(error -> 
                    System.err.println("Error fetching hotel details: " + error.getMessage()));
    }
}