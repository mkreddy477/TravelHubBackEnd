package com.example.travelhub.flightbooking.repository;

import com.example.travelhub.flightbooking.entity.BookingDetails;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * R2DBC Repository for BOOKING_DETAILS table
 */
@Repository
public interface BookingDetailsRepository extends R2dbcRepository<BookingDetails, Long> {
    
    Mono<BookingDetails> findByBookingReference(String bookingReference);
    
    Flux<BookingDetails> findByUserId(Long userId);
}
