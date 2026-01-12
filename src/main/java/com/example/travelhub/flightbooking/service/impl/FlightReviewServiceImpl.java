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
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.travelhub.flightbooking.exception.FlightServiceException;
import com.example.travelhub.flightbooking.exception.GlobalExceptionHandler;
import com.example.travelhub.flightbooking.models.reviewmodels.ReviewRequest;
import com.example.travelhub.flightbooking.models.reviewmodels.ReviewResponse;
import com.example.travelhub.flightbooking.service.FlightReviewService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

/**
 * Implementation of FlightReviewService using WebFlux WebClient
 */
@Slf4j
@Service
public class FlightReviewServiceImpl implements FlightReviewService {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    private final WebClient webClient;
    private final String reviewApiUrl;
    private final ObjectMapper objectMapper;

    public FlightReviewServiceImpl(
            ObjectMapper objectMapper,
            @Value("${tripjack.api.base-url}") String baseUrl,
            @Value("${tripjack.api.flightsreviewapi}") String reviewEndpoint,
            @Value("${tripjack.api-key}") String apiKey) {
        
        this.reviewApiUrl = reviewEndpoint;
        this.objectMapper = objectMapper;
        
        // Configure Jackson decoder to accept application/octet-stream as JSON
        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> {
                configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024);
                configurer.defaultCodecs().jackson2JsonDecoder(
                    new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM)
                );
            })
            .build();
        
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(strategies)
                .defaultHeader("apikey", apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
        
        log.info("FlightReviewService initialized with base URL: {} and review endpoint: {}", baseUrl, reviewEndpoint);
    }

    @Override
    public Mono<ReviewResponse> getFlightReview(ReviewRequest reviewRequest) {
        log.info("Fetching flight review for price IDs: {}", reviewRequest.getPriceIds());
        
        return webClient
                .post()
                .uri(reviewApiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(reviewRequest)
                .retrieve()
                .onStatus(
                    HttpStatusCode::is4xxClientError,
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Client error (4xx) occurred: {}", errorBody);
                            return Mono.error(new FlightServiceException(
                                "Client error during flight review: " + errorBody,
                                response.statusCode().value()
                            ));
                        })
                )
                .onStatus(
                    HttpStatusCode::is5xxServerError,
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Server error (5xx) occurred: {}", errorBody);
                            return Mono.error(new FlightServiceException(
                                "Server error during flight review: " + errorBody,
                                response.statusCode().value()
                            ));
                        })
                )
                .bodyToMono(ReviewResponse.class)
                .timeout(Duration.ofSeconds(30))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                    .filter(throwable -> !(throwable instanceof WebClientResponseException.BadRequest))
                    .doBeforeRetry(retrySignal -> 
                        log.warn("Retrying flight review request. Attempt: {}", retrySignal.totalRetries() + 1)
                    )
                )
                .doOnSuccess(response -> 
                    log.info("Successfully fetched flight review. Booking ID: {}",response)
                )
                .doOnError(error -> 
                    log.error("Error fetching flight review: {}", error.getMessage(), error)
                )
                .onErrorMap(throwable -> {
                    if (throwable instanceof FlightServiceException) {
                        return throwable;
                    }
                    return new FlightServiceException(
                        "Unexpected error during flight review: " + throwable.getMessage(),
                        500
                    );
                });
    }
}
