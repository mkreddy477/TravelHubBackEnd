package com.example.travelhub.hotelbooking.service;

import com.example.travelhub.locationservice.dto.HotelSearchRequest;
import com.example.travelhub.hotelbooking.models.HotelSearchResponse;
import reactor.core.publisher.Mono;

public interface HotelSearchService {
    Mono<HotelSearchResponse> search(HotelSearchRequest request);
}