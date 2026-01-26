package com.example.travelhub.hotelbooking.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.travelhub.hotelbooking.models.CancellationPolicyResponse;
import com.example.travelhub.hotelbooking.models.HotelOperationRequest;
import com.example.travelhub.hotelbooking.service.HotelCancellationPolicyService;

import reactor.core.publisher.Mono;

@Service
public class HotelCancellationPolicyServiceImpl implements HotelCancellationPolicyService {
	@Value("${hotel.api.cancellation.policy.url}") String baseUrl;

    private final WebClient webClient;

    public HotelCancellationPolicyServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.your-hotel-provider.com").build();
    }

    @Override
    public Mono<CancellationPolicyResponse> getCancellationPolicy(HotelOperationRequest request) {
       
        
        return webClient.post()
                .uri(baseUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CancellationPolicyResponse.class)
                .doOnSuccess(response -> 
                    System.out.println("Successfully fetched cancellation policy for: " + request.getId()))
                .doOnError(error -> 
                    System.err.println("Error fetching cancellation policy: " + error.getMessage()));
    }
}