package com.example.travelhub.hotelbooking.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.travelhub.hotelbooking.models.HotelSearchRequest;
import com.example.travelhub.hotelbooking.models.HotelSearchResponse;
import com.example.travelhub.hotelbooking.service.HotelSearchService;

import reactor.core.publisher.Mono;

@Service
public class HotelSearchServiceImpl implements HotelSearchService {

    private final WebClient webClient;
    private final String apiKey;

    public HotelSearchServiceImpl(
            WebClient.Builder webClientBuilder,
            @Value("${hotel.api.base-url}") String baseUrl,
            @Value("${hotel.api.key}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.apiKey = apiKey;
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
        
        return webClient.post()
                .uri("/search")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(HotelSearchResponse.class)
                .doOnSuccess(response -> {
                    System.out.println("Successfully completed hotel search");
                    if (response.getSearchResult() != null) {
                        System.out.println("  - Total hotels found: " + response.getSearchResult().getSize());
                        System.out.println("  - Hotels in response: " + 
                            (response.getSearchResult().getHis() != null 
                                ? response.getSearchResult().getHis().size() : 0));
                        
                        if (response.getSearchResult().getHis() != null 
                                && !response.getSearchResult().getHis().isEmpty()) {
                            System.out.println("  - Sample hotel: " + 
                                response.getSearchResult().getHis().get(0).getName());
                        }
                    }
                })
                .doOnError(error -> 
                    System.err.println("Error during hotel search: " + error.getMessage()));
    }
}