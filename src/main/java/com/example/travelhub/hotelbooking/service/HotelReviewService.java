package com.example.travelhub.hotelbooking.service;

import com.example.travelhub.hotelbooking.models.HotelReviewRequest;
import com.example.travelhub.hotelbooking.models.HotelReviewResponse;

import reactor.core.publisher.Mono;

public interface HotelReviewService {
    Mono<HotelReviewResponse> reviewHotel(HotelReviewRequest request);
}