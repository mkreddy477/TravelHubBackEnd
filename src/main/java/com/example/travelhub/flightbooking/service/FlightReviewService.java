package com.example.travelhub.flightbooking.service;

import org.springframework.stereotype.Service;

import com.example.travelhub.flightbooking.models.reviewmodels.ReviewRequest;
import com.example.travelhub.flightbooking.models.reviewmodels.ReviewResponse;

import reactor.core.publisher.Mono;

/**
 * Service interface for TripJack Flight Review operations
 */
@Service
public interface FlightReviewService {
 
    Mono<ReviewResponse> getFlightReview(ReviewRequest reviewRequest);
}
