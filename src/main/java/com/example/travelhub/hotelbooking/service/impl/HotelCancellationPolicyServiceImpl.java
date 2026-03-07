package com.example.travelhub.hotelbooking.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.travelhub.hotelbooking.models.CancellationPolicyResponse;
import com.example.travelhub.hotelbooking.models.CancellationPolicyRequest;
import com.example.travelhub.hotelbooking.service.HotelCancellationPolicyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class HotelCancellationPolicyServiceImpl implements HotelCancellationPolicyService{
    
    private static final Logger log = LoggerFactory.getLogger(HotelCancellationPolicyServiceImpl.class);
    
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    
    public HotelCancellationPolicyServiceImpl(
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper,
            @Value("${hotel.api.cancellation.policy.url}") String baseUrl,
            @Value("${hotel.api.key}") String apiKey) {
    	
    	log.info("ApiKey:: {}, BaseURL:: {}", apiKey);
    	ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024); // 10 MB
                    configurer.defaultCodecs().jackson2JsonDecoder(
                        new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM)
                    );
                })
                .build();
    	
        this.webClient = webClientBuilder
        		.baseUrl(baseUrl)
                .exchangeStrategies(strategies)
                .build();
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }
    
    public Mono<CancellationPolicyResponse> getCancellationPolicy(CancellationPolicyRequest request) {
        log.info("Fetching cancellation policy for hotel: {}, option: {}, cancellationPolicyId: {}", 
            request.getId(), request.getOptionId());
        
        return webClient
            .post()
            .uri("")
            .header("apikey", apiKey)  // ✅ Changed from "Authorization: Bearer" to "apikey"
            .header("Content-Type", "application/json")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(String.class)  // ⭐ Get as String to avoid Content-Type issues
            .flatMap(responseBody -> {
                try {
                    log.debug("Raw cancellation policy response: {}", responseBody);
                    
                    // Parse JSON manually
                    CancellationPolicyResponse response = 
                        objectMapper.readValue(responseBody, CancellationPolicyResponse.class);
                    
                    log.info("Successfully parsed cancellation policy response for hotel: {}", request.getId());
                    return Mono.just(response);
                    
                } catch (JsonProcessingException e) {
                    log.error("Failed to parse cancellation policy response. Raw response: {}", responseBody, e);
                    return Mono.error(new RuntimeException("Invalid JSON response from TripJack API", e));
                }
            })
            .doOnError(error -> 
                log.error("Error fetching cancellation policy for hotel: {}, option: {}", 
                    request.getId(), request.getOptionId(), error)
            );
    }
}