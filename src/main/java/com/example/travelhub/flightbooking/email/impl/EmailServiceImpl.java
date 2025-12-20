package com.example.travelhub.flightbooking.email.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.travelhub.flightbooking.email.BookingConfirmationEmail;
import com.example.travelhub.flightbooking.email.EmailService;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Implementation of EmailService using Spring Mail and Thymeleaf templates
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final String fromAddress;
    private final String fromName;

    public EmailServiceImpl(
            JavaMailSender mailSender,
            TemplateEngine templateEngine,
            @Value("${app.mail.from-address}") String fromAddress,
            @Value("${app.mail.from-name}") String fromName) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.fromAddress = fromAddress;
        this.fromName = fromName;
    }

    @Override
    public Mono<Void> sendBookingConfirmation(BookingConfirmationEmail bookingConfirmation) {
        return Mono.fromRunnable(() -> {
            try {
                String htmlContent = buildBookingConfirmationHtml(bookingConfirmation);
                String subject = String.format("Booking Confirmed - %s to %s | PNR: %s",
                        bookingConfirmation.getOrigin(),
                        bookingConfirmation.getDestination(),
                        bookingConfirmation.getPnr() != null ? bookingConfirmation.getPnr() : bookingConfirmation.getBookingId());

                sendHtmlEmailSync(bookingConfirmation.getRecipientEmail(), subject, htmlContent);
                
                log.info("Booking confirmation email sent successfully to {} for bookingId: {}",
                        bookingConfirmation.getRecipientEmail(), bookingConfirmation.getBookingId());
                        
            } catch (Exception e) {
                log.error("Failed to send booking confirmation email to {} for bookingId: {}",
                        bookingConfirmation.getRecipientEmail(), bookingConfirmation.getBookingId(), e);
                throw new RuntimeException("Failed to send booking confirmation email", e);
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    @Override
    public Mono<Void> sendSimpleEmail(String to, String subject, String body) {
        return Mono.fromRunnable(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(String.format("%s <%s>", fromName, fromAddress));
                message.setTo(to);
                message.setSubject(subject);
                message.setText(body);
                
                mailSender.send(message);
                log.info("Simple email sent successfully to {}", to);
                
            } catch (Exception e) {
                log.error("Failed to send simple email to {}: {}", to, e.getMessage(), e);
                throw new RuntimeException("Failed to send email", e);
            }
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    @Override
    public Mono<Void> sendHtmlEmail(String to, String subject, String htmlContent) {
        return Mono.fromRunnable(() -> {
            sendHtmlEmailSync(to, subject, htmlContent);
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    private void sendHtmlEmailSync(String to, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            helper.setFrom(String.format("%s <%s>", fromName, fromAddress));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(mimeMessage);
            log.info("HTML email sent successfully to {}", to);
            
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    private String buildBookingConfirmationHtml(BookingConfirmationEmail booking) {
        Context context = new Context();
        context.setVariable("booking", booking);
        context.setVariable("bookingId", booking.getBookingId());
        context.setVariable("pnr", booking.getPnr());
        context.setVariable("origin", booking.getOrigin());
        context.setVariable("destination", booking.getDestination());
        context.setVariable("departureDate", booking.getDepartureDate());
        context.setVariable("departureTime", booking.getDepartureTime());
        context.setVariable("arrivalDate", booking.getArrivalDate());
        context.setVariable("arrivalTime", booking.getArrivalTime());
        context.setVariable("airlineName", booking.getAirlineName());
        context.setVariable("flightNumber", booking.getFlightNumber());
        context.setVariable("cabinClass", booking.getCabinClass());
        context.setVariable("passengers", booking.getPassengers());
        context.setVariable("totalFare", booking.getTotalFare());
        context.setVariable("currency", booking.getCurrency());
        context.setVariable("recipientName", booking.getRecipientName());
        context.setVariable("bookingStatus", booking.getBookingStatus());
        
        return templateEngine.process("booking-confirmation", context);
    }
}
