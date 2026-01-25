package com.example.travelhub.flightbooking.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelhub.flightbooking.models.reviewmodels.ReviewRequest;
import com.example.travelhub.flightbooking.models.reviewmodels.ReviewResponse;
import com.example.travelhub.flightbooking.service.FlightReviewService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * REST Controller for Flight Review operations using WebFlux
 */
@Slf4j
@RestController
@RequestMapping({"/api/v1/flights", "/api/flights"})
public class FlightReviewController {
    private static final Logger log = LoggerFactory.getLogger(FlightReviewController.class);

    private final FlightReviewService flightReviewService ;
    
    

    public FlightReviewController(FlightReviewService flightReviewService) {
		super();
		this.flightReviewService = flightReviewService;
	}

	/**
     * Get flight review details for given price IDs
     * 
     * @param reviewRequest containing list of price IDs
     * @return Mono of ReviewResponse
     */
    @PostMapping(
        value = "/review",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<ReviewResponse>> getFlightReview(
            @Valid @RequestBody ReviewRequest reviewRequest) {
    	System.out.println("priceids"+reviewRequest.getPriceIds());
        System.out.println("review page testing");
        log.info("Received flight review request for {} price IDs", 
                reviewRequest.getPriceIds() != null ? reviewRequest.getPriceIds().size() : 0);
        
        return flightReviewService.getFlightReview(reviewRequest)
                .map(response -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnSuccess(response -> 
                    log.info("Flight review request completed successfully")
                )
                .doOnError(error -> 
                    log.error("Flight review request failed: {}", error.getMessage())
                );
    }

    /**
     * Alternative endpoint that returns just the response body (common pattern)
     * 
     * @param reviewRequest containing list of price IDs
     * @return Mono of ReviewResponse
     */
    @PostMapping(
        value = "/review-direct",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<ReviewResponse> getFlightReviewDirect(
            @Valid @RequestBody ReviewRequest reviewRequest) {
        
        log.info("Received direct flight review request");
        
        return flightReviewService.getFlightReview(reviewRequest);
    }
}
