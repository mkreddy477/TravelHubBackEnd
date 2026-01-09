package com.example.travelhub.hotelbooking.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.travelhub.hotelbooking.models.HotelDetailResponse;
import com.example.travelhub.hotelbooking.models.HotelOperationRequest;
import com.example.travelhub.hotelbooking.service.HotelDetailSearchService;

import reactor.core.publisher.Mono;

@Service
public class HotelDetailSearchServiceImpl implements HotelDetailSearchService {

    private final WebClient webClient;
    private final String apiKey;

    public HotelDetailSearchServiceImpl(
            WebClient.Builder webClientBuilder,
            @Value("${hotel.api.base-url}") String baseUrl,
            @Value("${hotel.api.key}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
    }

    @Override
    public Mono<HotelDetailResponse> getDetails(HotelOperationRequest request) {
        System.out.println("Fetching hotel details for Hotel ID: " + request.getId());
        
        return webClient.post()
                .uri("/detail")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(HotelDetailResponse.class)
                .doOnSuccess(response -> {
                    System.out.println("Successfully fetched hotel details");
                    if (response.getHotel() != null) {
                        System.out.println("  - Hotel ID: " + response.getHotel().getId());
                        System.out.println("  - Hotel Name: " + response.getHotel().getName());
                        System.out.println("  - Rating: " + response.getHotel().getRt());
                        System.out.println("  - Property Type: " + response.getHotel().getPt());
                        System.out.println("  - Options available: " + 
                            (response.getHotel().getOps() != null 
                                ? response.getHotel().getOps().size() : 0));
                        System.out.println("  - Instructions: " + 
                            (response.getHotel().getInst() != null 
                                ? response.getHotel().getInst().size() : 0));
                    }
                })
                .doOnError(error -> 
                    System.err.println("Error fetching hotel details: " + error.getMessage()));
    }
}