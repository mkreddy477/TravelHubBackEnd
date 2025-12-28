package com.example.travelhub.flightbooking.service;

import com.example.travelhub.flightbooking.entity.BookingDetails;
import com.example.travelhub.flightbooking.entity.UserDetails;
import com.example.travelhub.flightbooking.models.bookingmodels.BookingRequest;
import com.example.travelhub.flightbooking.models.bookingmodels.BookingResponse;
import reactor.core.publisher.Mono;

/**
 * Service interface for persisting booking data to database
 */
public interface BookingDatabaseService {

    /**
     * Save booking data to database (USER_DETAILS, BOOKING_DETAILS, PAYMENT_DETAILS)
     * 
     * @param bookingRequest the original booking request
     * @param bookingResponse the response from TripJack API
     * @return Mono<BookingDetails> the saved booking details
     */
    Mono<BookingDetails> saveBookingData(BookingRequest bookingRequest, BookingResponse bookingResponse);

    /**
     * Find or create user based on contact info
     * 
     * @param bookingRequest containing contact and traveller info
     * @return Mono<UserDetails> the found or created user
     */
    Mono<UserDetails> findOrCreateUser(BookingRequest bookingRequest);
}
