package com.example.travelhub.hotelbooking.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.travelhub.hotelbooking.models.CancellationPolicyResponse;
import com.example.travelhub.hotelbooking.models.HotelOperationRequest;
import com.example.travelhub.hotelbooking.service.HotelCancellationPolicyService;

import reactor.core.publisher.Mono;

@Service
public class HotelCancellationPolicyServiceImpl implements HotelCancellationPolicyService {

    private final WebClient webClient;

    public HotelCancellationPolicyServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.your-hotel-provider.com").build();
    }

    @Override
    public Mono<CancellationPolicyResponse> getCancellationPolicy(HotelOperationRequest request) {
        System.out.println("Fetching cancellation policy for Hotel ID: " + request.getId() 
                + ", Option ID: " + request.getOptionId());
        
        return webClient.post()
                .uri("/api/v1/cancellation-policy")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CancellationPolicyResponse.class)
                .doOnSuccess(response -> 
                    System.out.println("Successfully fetched cancellation policy for: " + request.getId()))
                .doOnError(error -> 
                    System.err.println("Error fetching cancellation policy: " + error.getMessage()));
    }
}