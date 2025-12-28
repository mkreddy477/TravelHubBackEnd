package com.example.travelhub.flightbooking.repository;

import com.example.travelhub.flightbooking.entity.PaymentDetails;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * R2DBC Repository for PAYMENT_DETAILS table
 */
@Repository
public interface PaymentDetailsRepository extends R2dbcRepository<PaymentDetails, Long> {
    
    Flux<PaymentDetails> findByBookingDetailId(Long bookingDetailId);
}
