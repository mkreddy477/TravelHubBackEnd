package com.example.travelhub.flightbooking.service.impl;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.travelhub.flightbooking.exception.FlightServiceException;
import com.example.travelhub.flightbooking.models.farerulemodels.FareRuleRequest;
import com.example.travelhub.flightbooking.service.FareRuleService;
import com.fasterxml.jackson.databind.JsonNode;

import reactor.core.publisher.Mono;

@Service
public class FareRuleServiceImpl implements FareRuleService {

    private static final Logger log = LoggerFactory.getLogger(FareRuleServiceImpl.class);

    private final WebClient webClient;
    private final String fareRuleEndpoint;

    public FareRuleServiceImpl(
    		WebClient.Builder webClientBuilder,
            com.fasterxml.jackson.databind.ObjectMapper objectMapper,
            @Value("${tripjack.api.base-url}") String baseUrl,
            @Value("${tripjack.api.fareRuleapi}") String fareRuleEndpoint,
            @Value("${tripjack.api-key}") String apiKey) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Missing Tripjack API key property 'tripjack.api-key'");
        }
        this.fareRuleEndpoint = fareRuleEndpoint;
        
        // Configure Jackson decoder to accept application/octet-stream as JSON
        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> {
                configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024);
                configurer.defaultCodecs().jackson2JsonDecoder(
                    new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM)
                );
            })
            .build();
        
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .exchangeStrategies(strategies)
                .defaultHeader("apikey", apiKey.trim())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        log.info("FareRuleService initialized with base URL: {} and endpoint: {}", baseUrl, fareRuleEndpoint);
    }

    @Override
    public Mono<JsonNode> getFareRule(FareRuleRequest request) {
        return webClient
                .post()
                .uri(fareRuleEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new FlightServiceException(
                                        "Client error during fare rule: " + errorBody,
                                        response.statusCode().value())))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new FlightServiceException(
                                        "Server error during fare rule: " + errorBody,
                                        response.statusCode().value())))
                )
                .bodyToMono(JsonNode.class)
                .timeout(Duration.ofSeconds(300));
    }
}
