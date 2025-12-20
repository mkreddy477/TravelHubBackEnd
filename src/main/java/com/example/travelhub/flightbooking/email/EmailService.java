package com.example.travelhub.flightbooking.email;

import reactor.core.publisher.Mono;

/**
 * Service interface for sending emails
 */
public interface EmailService {
    
    /**
     * Send booking confirmation email to the user
     * @param bookingConfirmation booking details for the email
     * @return Mono<Void> completing when email is sent
     */
    Mono<Void> sendBookingConfirmation(BookingConfirmationEmail bookingConfirmation);
    
    /**
     * Send a simple text email
     * @param to recipient email address
     * @param subject email subject
     * @param body email body text
     * @return Mono<Void> completing when email is sent
     */
    Mono<Void> sendSimpleEmail(String to, String subject, String body);
    
    /**
     * Send an HTML email
     * @param to recipient email address
     * @param subject email subject
     * @param htmlContent HTML content of the email
     * @return Mono<Void> completing when email is sent
     */
    Mono<Void> sendHtmlEmail(String to, String subject, String htmlContent);
}
