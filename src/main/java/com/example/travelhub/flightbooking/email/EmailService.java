package com.example.travelhub.flightbooking.email;

import reactor.core.publisher.Mono;

/**
 * Service interface for sending emails
 */
public interface EmailService {
    
   
    Mono<Void> sendBookingConfirmation(BookingConfirmationEmail bookingConfirmation);
    
    
    Mono<Void> sendSimpleEmail(String to, String subject, String body);
    
   
    Mono<Void> sendHtmlEmail(String to, String subject, String htmlContent);
}
