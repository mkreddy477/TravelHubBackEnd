package com.example.travelhub.hotelbooking.service.impl;

import com.example.travelhub.hotelbooking.models.HotelBookingRequest;
import com.example.travelhub.hotelbooking.models.HotelBookingResponse;
import com.example.travelhub.hotelbooking.service.HotelBookingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class HotelBookingServiceImpl implements HotelBookingService {

    private final WebClient webClient;
    private final String apiKey;
    private final String bookingUrl;
    private final ObjectMapper objectMapper;

    public HotelBookingServiceImpl(
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper,
            @Value("${hotel.api.booking-url}") String bookingUrl,
            @Value("${hotel.api.key}") String apiKey) {
        
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
        this.bookingUrl = bookingUrl;
        this.apiKey = apiKey;
    }

    @Override
    public Mono<HotelBookingResponse> createBooking(HotelBookingRequest request) {
        log.info("=".repeat(80));
        log.info("CREATING HOTEL BOOKING");
        log.info("Booking ID: {}", request.getBookingId());
        log.info("Number of Rooms: {}", request.getRoomTravellerInfo().size());
        log.info("=".repeat(80));
        
        // Log the request
        try {
            String requestJson = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(request);
            log.info("TripJack Booking Request JSON:\n{}", requestJson);
        } catch (JsonProcessingException e) {
            log.error("Error serializing booking request", e);
        }

        return webClient.post()
                .uri(bookingUrl)
                .header("apikey", apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(rawResponse -> {
                    log.info("=".repeat(80));
                    log.info("RAW TRIPJACK BOOKING RESPONSE:");
                    log.info("=".repeat(80));
                    log.info(rawResponse);
                    log.info("=".repeat(80));
                    
                    // Save to file for debugging
                    try {
                        java.nio.file.Files.writeString(
                            java.nio.file.Paths.get("tripjack-booking-response.json"),
                            rawResponse,
                            java.nio.charset.StandardCharsets.UTF_8
                        );
                        log.info("✅ Booking response saved to: tripjack-booking-response.json");
                    } catch (Exception e) {
                        log.warn("Could not save booking response to file: {}", e.getMessage());
                    }
                })
                .flatMap(rawResponse -> {
                    try {
                        HotelBookingResponse response = objectMapper.readValue(
                            rawResponse, 
                            HotelBookingResponse.class
                        );
                        
                        log.info("=".repeat(80));
                        log.info("PARSED BOOKING RESPONSE:");
                        log.info("=".repeat(80));
                        log.info("Booking ID: {}", response.getBookingId());
                        
                        if (response.getStatus() != null) {
                            log.info("Status Success: {}", response.getStatus().getSuccess());
                            log.info("HTTP Status: {}", response.getStatus().getHttpStatus());
                        }
                        
                        log.info("=".repeat(80));
                        
                        return Mono.just(response);
                        
                    } catch (JsonProcessingException e) {
                        log.error("❌ Error parsing booking response", e);
                        return Mono.error(e);
                    }
                })
                .doOnSuccess(response -> {
                    log.info("✅ Hotel booking created successfully: {}", response.getBookingId());
                })
                .doOnError(WebClientResponseException.class, error -> {
                    log.error("=".repeat(80));
                    log.error("❌ TRIPJACK BOOKING API ERROR:");
                    log.error("Status: {}", error.getStatusCode());
                    log.error("Response Body: {}", error.getResponseBodyAsString());
                    log.error("=".repeat(80));
                })
                .doOnError(error -> {
                    if (!(error instanceof WebClientResponseException)) {
                        log.error("❌ Error creating hotel booking: {}", error.getMessage(), error);
                    }
                });
    }
}