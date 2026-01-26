package com.example.travelhub.hotelbooking.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.travelhub.hotelbooking.models.HotelReviewRequest;
import com.example.travelhub.hotelbooking.models.HotelReviewResponse;
import com.example.travelhub.hotelbooking.service.HotelReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class HotelReviewServiceImpl implements HotelReviewService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    @Value("${tripjack.api.key}")
    private String apiKey;
    
    @Value("${hotel.api.review.url}")
    private String reviewUrl;

    public HotelReviewServiceImpl(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<HotelReviewResponse> reviewHotel(HotelReviewRequest request) {
        log.info("Reviewing hotel: hotelId={}, optionId={}", request.getHotelId(), request.getOptionId());
        
        return webClient.post()
                .uri(reviewUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("apikey", apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        log.info("Raw Review Response: {}", response);
                        HotelReviewResponse reviewResponse = objectMapper.readValue(response, HotelReviewResponse.class);
                        log.info("Hotel Review successful. BookingId: {}", reviewResponse.getBookingId());
                        return Mono.just(reviewResponse);
                    } catch (Exception e) {
                        log.error("Error parsing hotel review response", e);
                        return Mono.error(new RuntimeException("Failed to parse hotel review response: " + e.getMessage()));
                    }
                })
                .doOnError(error -> log.error("Error during hotel review", error));
    }
}