package com.example.travelhub.hotelbooking.service;

import com.example.travelhub.hotelbooking.models.HotelDetailResponse;
import com.example.travelhub.hotelbooking.models.HotelOperationRequest;
import reactor.core.publisher.Mono;

public interface HotelDetailSearchService {
    Mono<HotelDetailResponse> getDetails(HotelOperationRequest request);
}