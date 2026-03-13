package com.example.travelhub.hotelbooking.service;

import com.example.travelhub.hotelbooking.models.HotelBookingRequest;
import com.example.travelhub.hotelbooking.models.HotelBookingResponse;
import reactor.core.publisher.Mono;

public interface HotelBookingService {
    
    /**
     * Create a hotel booking
     * 
     * @param request Hotel booking request with traveller and payment info
     * @return Mono of HotelBookingResponse with booking confirmation
     */
    Mono<HotelBookingResponse> createBooking(HotelBookingRequest request);
}