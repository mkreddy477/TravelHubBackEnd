package com.example.travelhub.flightbooking.service;

import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebClientConfig {

    /**
     * Global WebClient customizer that configures ALL WebClient.Builder instances
     * to accept application/octet-stream as JSON (TripJack API returns this content type)
     */
    @Bean
    public WebClientCustomizer webClientCustomizer(ObjectMapper objectMapper) {
        return webClientBuilder -> {
            ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024); // 10 MB
                    configurer.defaultCodecs().jackson2JsonDecoder(
                        new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM)
                    );
                })
                .build();
            webClientBuilder.exchangeStrategies(strategies);
        };
    }
}