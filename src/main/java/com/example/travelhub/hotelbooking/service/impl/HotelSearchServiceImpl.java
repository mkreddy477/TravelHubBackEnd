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
        this.objectMapper = objectMapper.copy()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Mono<HotelSearchResponse> search(HotelSearchRequest request) {
     
       
        
        return webClient.post()
                .uri("")
                .header("apikey", apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(rawResponse -> {
                    System.out.println(rawResponse.substring(0, Math.min(2000, rawResponse.length())));
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