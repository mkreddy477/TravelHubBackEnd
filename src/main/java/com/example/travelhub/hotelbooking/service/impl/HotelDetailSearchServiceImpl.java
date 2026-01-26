package com.example.travelhub.hotelbooking.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.travelhub.hotelbooking.models.Hotel;
import com.example.travelhub.hotelbooking.models.HotelDetailOption;
import com.example.travelhub.hotelbooking.models.HotelDetailResponse;
import com.example.travelhub.hotelbooking.models.HotelOperationRequest;
import com.example.travelhub.hotelbooking.service.HotelDetailSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class HotelDetailSearchServiceImpl implements HotelDetailSearchService {

    private final WebClient webClient;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public HotelDetailSearchServiceImpl(
    		WebClient.Builder webClientBuilder,
    		ObjectMapper objectMapper,
            @Value("${hotel.api.search-detail-url}") String baseUrl,
            @Value("${hotel.api.key}") String apiKey) {
    	this.objectMapper = objectMapper; 
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
        
        // TripJack only requires the id field
        Map<String, String> tripJackRequest = Map.of("id", request.getId());
        System.out.println("***************"+tripJackRequest);
        System.out.println("##################");
        return webClient.post()
                .uri("")
                .header("apikey", apiKey)  // ✅ Changed from "Authorization: Bearer" to "apikey"
                .header("Content-Type", "application/json")
                .bodyValue(tripJackRequest)
                .retrieve()
                .bodyToMono(String.class)  // ✅ Get raw response first
                .doOnNext(rawResponse -> {
                    System.out.println("=".repeat(80));
                    System.out.println("RAW TRIPJACK HOTEL DETAIL RESPONSE:");
                    System.out.println("=".repeat(80));
                    System.out.println(rawResponse);
                    System.out.println("=".repeat(80));
                    
                    // Also save to file for easier viewing
                    try {
                        java.nio.file.Files.writeString(
                            java.nio.file.Paths.get("tripjack-hotel-detail-response.json"),
                            rawResponse,
                            java.nio.charset.StandardCharsets.UTF_8
                        );
                        System.out.println("✅ Response saved to: tripjack-hotel-detail-response.json");
                    } catch (Exception e) {
                        System.err.println("Could not save response to file: " + e.getMessage());
                    }
                })
                .flatMap(rawResponse -> {
                    try {
                    	System.out.println(rawResponse);
                        // Parse the response
                        HotelDetailResponse response = objectMapper.readValue(
                            rawResponse, 
                            HotelDetailResponse.class
                        );
                        
                        System.out.println("=".repeat(80));
                        System.out.println("PARSED HOTEL DETAIL RESPONSE:");
                        System.out.println("=".repeat(80));
                        
                        // Log status
                        if (response.getStatus() != null) {
                            System.out.println("Status:");
                            System.out.println("  - Success: " + response.getStatus().getSuccess());
                            System.out.println("  - HTTP Status: " + response.getStatus().getHttpStatus());
                        } else {
                            System.out.println("Status: NULL");
                        }
                        
                        // Log hotel
                        if (response.getHotel() != null) {
                            Hotel hotel = response.getHotel();
                            System.out.println("Hotel:");
                            System.out.println("  - ID: " + hotel.getId());
                            System.out.println("  - UID: " + hotel.getUid());
                            System.out.println("  - Name: " + hotel.getName());
                            System.out.println("  - Rating: " + hotel.getRt());
                            System.out.println("  - Property Type: " + hotel.getPt());
                            
                            // Address
                            if (hotel.getAd() != null) {
                                System.out.println("  - Address: " + hotel.getAd().getAdr());
                                System.out.println("  - City: " + hotel.getAd().getCtn());
                                System.out.println("  - Country: " + hotel.getAd().getCn());
                            } else {
                                System.out.println("  - Address: NULL");
                            }
                            
                            // Instructions
                            System.out.println("  - Instructions: " + 
                                (hotel.getInst() != null ? hotel.getInst().size() : 0));
                            
                            // Options
                            System.out.println("  - Options: " + 
                                (hotel.getOps() != null ? hotel.getOps().size() : 0));
                            
                            if (hotel.getOps() != null && !hotel.getOps().isEmpty()) {
                                System.out.println("  - First Option Details:");
                                HotelDetailOption firstOption = hotel.getOps().get(0);
                                System.out.println("    • Option ID: " + firstOption.getId());
                                System.out.println("    • Total Price: " + firstOption.getTp());
                                System.out.println("    • Rooms: " + 
                                    (firstOption.getRis() != null ? firstOption.getRis().size() : 0));
                            }
                        } else {
                            System.out.println("Hotel: NULL ❌");
                            System.out.println("⚠️  WARNING: Hotel data is NULL in the response!");
                        }
                        
                        System.out.println("=".repeat(80));
                        
                        return Mono.just(response);
                        
                    } catch (JsonProcessingException e) {
                        System.err.println("❌ JSON PARSING ERROR:");
                        System.err.println("Error message: " + e.getMessage());
                        e.printStackTrace();
                        return Mono.error(e);
                    }
                }).doOnError(WebClientResponseException.class, error -> {
                    System.err.println("=".repeat(80));
                    System.err.println("❌ TRIPJACK API HTTP ERROR:");
                    System.err.println("Status: " + error.getStatusCode());
                    System.err.println("Response Body:");
                    System.err.println(error.getResponseBodyAsString());
                    System.err.println("=".repeat(80));
                })
                .doOnError(error -> {
                    if (!(error instanceof WebClientResponseException)) {
                        System.err.println("=".repeat(80));
                        System.err.println("❌ ERROR FETCHING HOTEL DETAILS:");
                        System.err.println("Error type: " + error.getClass().getName());
                        System.err.println("Error message: " + error.getMessage());
                        error.printStackTrace();
                        System.err.println("=".repeat(80));
                    }
                });
    }
}