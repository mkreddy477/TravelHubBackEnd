package com.example.travelhub.flightbooking.service.impl;

import java.time.Duration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.travelhub.flightbooking.exception.FlightServiceException;
import com.example.travelhub.flightbooking.email.BookingConfirmationEmail;
import com.example.travelhub.flightbooking.email.BookingEmailBuilder;
import com.example.travelhub.flightbooking.email.EmailService;
import com.example.travelhub.flightbooking.validation.BookingRequestValidator;
import com.example.travelhub.flightbooking.validation.ConfirmBookRequestValidator;
import com.example.travelhub.flightbooking.models.bookingmodels.BookingDetailsRequest;
import com.example.travelhub.flightbooking.models.bookingmodels.BookingDetailsResponse;
import com.example.travelhub.flightbooking.models.bookingmodels.BookingRequest;
import com.example.travelhub.flightbooking.models.bookingmodels.BookingResponse;
import com.example.travelhub.flightbooking.models.bookingmodels.ConfirmBookRequest;
import com.example.travelhub.flightbooking.models.bookingmodels.ConfirmBookResponse;
import com.example.travelhub.flightbooking.models.bookingmodels.FareValidateRequest;
import com.example.travelhub.flightbooking.models.bookingmodels.FareValidateResponse;
import com.example.travelhub.flightbooking.models.bookingmodels.ReleasePnrRequest;
import com.example.travelhub.flightbooking.models.bookingmodels.ReleasePnrResponse;
import com.example.travelhub.flightbooking.service.FlightBookingService;
import com.example.travelhub.flightbooking.service.BookingDatabaseService;

import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

/**
 * Implementation of FlightBookingService using WebFlux WebClient
 * API: https://apitest.tripjack.com/oms/v1/air/book
 */
@Service
public class FlightBookingServiceImpl implements FlightBookingService {
    
    private static final Logger log = LoggerFactory.getLogger(FlightBookingServiceImpl.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final BookingRequestValidator bookingRequestValidator;
    private final ConfirmBookRequestValidator confirmBookRequestValidator;
    private final EmailService emailService;
    private final BookingEmailBuilder bookingEmailBuilder;
    private final BookingDatabaseService bookingDatabaseService;
    private final WebClient webClient;
    private final String bookApiUrl;
    private final String fareValidateUrl;
    private final String confirmBookUrl;
    private final String bookingDetailsUrl;
    private final String unholdUrl;

    public FlightBookingServiceImpl(
            BookingRequestValidator bookingRequestValidator,
            ConfirmBookRequestValidator confirmBookRequestValidator,
            EmailService emailService,
            BookingEmailBuilder bookingEmailBuilder,
            BookingDatabaseService bookingDatabaseService,
            WebClient.Builder webClientBuilder,
            @Value("${tripjack.api.base-url}") String baseUrl,
            @Value("${tripjack.api.book-endpoint:/oms/v1/air/book}") String bookEndpoint,
            @Value("${tripjack.api.fare-validate-endpoint:/oms/v1/air/fare-validate}") String fareValidateEndpoint,
            @Value("${tripjack.api.confirm-book-endpoint:/oms/v1/air/confirm-book}") String confirmBookEndpoint,
            @Value("${tripjack.api.booking-details-endpoint:/oms/v1/booking-details}") String bookingDetailsEndpoint,
            @Value("${tripjack.api.unhold-endpoint:/oms/v1/air/unhold}") String unholdEndpoint,
            @Value("${tripjack.api-key}") String apiKey) {
        
        this.bookingRequestValidator = bookingRequestValidator;
        this.confirmBookRequestValidator = confirmBookRequestValidator;
        this.emailService = emailService;
        this.bookingEmailBuilder = bookingEmailBuilder;
        this.bookingDatabaseService = bookingDatabaseService;
        
        this.bookApiUrl = bookEndpoint;
        this.fareValidateUrl = fareValidateEndpoint;
        this.confirmBookUrl = confirmBookEndpoint;
        this.bookingDetailsUrl = bookingDetailsEndpoint;
        this.unholdUrl = unholdEndpoint;
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader("apikey", apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
        
        log.info("FlightBookingService initialized with base URL: {}", baseUrl);
    }

    @Override
    public Mono<BookingResponse> bookFlight(BookingRequest bookingRequest) {
        // Validate request before sending to TripJack
        List<String> validationErrors = bookingRequestValidator.validate(bookingRequest);
        if (!validationErrors.isEmpty()) {
            String errorMessage = "Booking request validation failed: " + String.join("; ", validationErrors);
            log.error(errorMessage);
            return Mono.error(new FlightServiceException(errorMessage, 400));
        }
        
        // Clean passport fields if incomplete (missing pid) - TripJack rejects incomplete passport info
        cleanIncompletePassportInfo(bookingRequest);
        
        // Always use /air/book for booking with traveller info
        // The bookingId from Review API is used with /air/book endpoint
        // /air/confirm-book is only for confirming a HELD booking (no traveller info, just payment)
        log.info("Processing flight booking for bookingId: {} (API: {})", 
                bookingRequest.getBookingId(), bookApiUrl);
        
        // Log exact JSON being sent to TripJack
        try {
            String jsonRequest = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bookingRequest);
            log.info("Book request JSON: {}", jsonRequest);
        } catch (JsonProcessingException e) {
            log.warn("Could not serialize request for logging", e);
        }
        
        return webClient
                .post()
                .uri(bookApiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookingRequest)
                .retrieve()
                .onStatus(
                    HttpStatusCode::is4xxClientError,
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Client error (4xx) occurred during booking: {}", errorBody);
                            return Mono.error(new FlightServiceException(
                                "Client error during flight booking: " + errorBody,
                                response.statusCode().value()
                            ));
                        })
                )
                .onStatus(
                    HttpStatusCode::is5xxServerError,
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Server error (5xx) occurred during booking: {}", errorBody);
                            return Mono.error(new FlightServiceException(
                                "Server error during flight booking: " + errorBody,
                                response.statusCode().value()
                            ));
                        })
                )
                .bodyToMono(BookingResponse.class)
                .timeout(Duration.ofSeconds(60)) // Booking may take longer
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(3))
                    .filter(throwable -> !(throwable instanceof FlightServiceException) && !(throwable instanceof WebClientResponseException.BadRequest))
                    .doBeforeRetry(retrySignal -> 
                        log.warn("Retrying flight booking request. Attempt: {}", retrySignal.totalRetries() + 1)
                    )
                )
                .flatMap(response -> {
                    log.info("Successfully completed flight booking. BookingId: {}, Status: {}", 
                        response.getBookingId(), response.getStatus());
                    
                    // Log full response for debugging email issues
                    try {
                        String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
                        log.info("Full booking response: {}", jsonResponse);
                    } catch (JsonProcessingException e) {
                        log.warn("Could not serialize response for logging", e);
                    }
                    
                    // Save booking data to database asynchronously
                    return bookingDatabaseService.saveBookingData(bookingRequest, response)
                            .doOnSuccess(savedBooking -> 
                                log.info("Booking data saved to database. BookingDetailId: {}", savedBooking.getBookingDetailId())
                            )
                            .doOnError(dbError -> 
                                log.error("Failed to save booking data to database: {}", dbError.getMessage())
                            )
                            .onErrorResume(dbError -> Mono.empty()) // Don't fail booking if DB save fails
                            .thenReturn(response)
                            .doOnSuccess(r -> {
                                // Send booking confirmation email asynchronously
                                sendBookingConfirmationEmail(bookingRequest, response);
                            });
                })
                .doOnError(error -> 
                    log.error("Error during flight booking: {}", error.getMessage(), error)
                )
                .onErrorMap(throwable -> {
                    if (throwable instanceof FlightServiceException) {
                        return throwable;
                    }
                    return new FlightServiceException(
                        "Unexpected error during flight booking: " + throwable.getMessage(),
                        500
                    );
                });
       
        
    }

    @Override
    public Mono<BookingResponse> holdBooking(BookingRequest bookingRequest) {
        // Validate request before sending to TripJack (skip payment validation for hold)
        bookingRequest.setPaymentInfos(null); // Ensure paymentInfos is null for hold booking
        List<String> validationErrors = bookingRequestValidator.validateForHold(bookingRequest);
        if (!validationErrors.isEmpty()) {
            String errorMessage = "Hold booking request validation failed: " + String.join("; ", validationErrors);
            log.error(errorMessage);
            return Mono.error(new FlightServiceException(errorMessage, 400));
        }
        
        // Clean passport fields if incomplete (missing pid) - TripJack rejects incomplete passport info
        cleanIncompletePassportInfo(bookingRequest);
        
        log.info("Processing hold booking for bookingId: {}", bookingRequest.getBookingId());
        
        return webClient
                .post()
                .uri(bookApiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookingRequest)
                .retrieve()
                .onStatus(
                    HttpStatusCode::is4xxClientError,
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Client error (4xx) occurred during hold booking: {}", errorBody);
                            return Mono.error(new FlightServiceException(
                                "Client error during hold booking: " + errorBody,
                                response.statusCode().value()
                            ));
                        })
                )
                .onStatus(
                    HttpStatusCode::is5xxServerError,
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Server error (5xx) occurred during hold booking: {}", errorBody);
                            return Mono.error(new FlightServiceException(
                                "Server error during hold booking: " + errorBody,
                                response.statusCode().value()
                            ));
                        })
                )
                .bodyToMono(BookingResponse.class)
                .timeout(Duration.ofSeconds(60))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(3))
                    .filter(throwable -> !(throwable instanceof FlightServiceException) && !(throwable instanceof WebClientResponseException.BadRequest))
                    .doBeforeRetry(retrySignal -> 
                        log.warn("Retrying hold booking request. Attempt: {}", retrySignal.totalRetries() + 1)
                    )
                )
                .doOnSuccess(response -> 
                    log.info("Successfully completed hold booking. BookingId: {}, Status: {}", 
                        response.getBookingId(), response.getStatus())
                )
                .doOnError(error -> 
                    log.error("Error during hold booking: {}", error.getMessage(), error)
                )
                .onErrorMap(throwable -> {
                    if (throwable instanceof FlightServiceException) {
                        return throwable;
                    }
                    return new FlightServiceException(
                        "Unexpected error during hold booking: " + throwable.getMessage(),
                        500
                    );
                });
    }

    @Override
    public Mono<FareValidateResponse> validateFare(FareValidateRequest fareValidateRequest) {
        log.info("Validating fare for bookingId: {}", fareValidateRequest.getBookingId());
        
        return webClient
                .post()
                .uri(fareValidateUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(fareValidateRequest)
                .retrieve()
                .onStatus(
                    HttpStatusCode::is4xxClientError,
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Client error (4xx) occurred during fare validation: {}", errorBody);
                            return Mono.error(new FlightServiceException(
                                "Client error during fare validation: " + errorBody,
                                response.statusCode().value()
                            ));
                        })
                )
                .onStatus(
                    HttpStatusCode::is5xxServerError,
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Server error (5xx) occurred during fare validation: {}", errorBody);
                            return Mono.error(new FlightServiceException(
                                "Server error during fare validation: " + errorBody,
                                response.statusCode().value()
                            ));
                        })
                )
                .bodyToMono(FareValidateResponse.class)
                .timeout(Duration.ofSeconds(30))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(2))
                    .filter(throwable -> !(throwable instanceof FlightServiceException) && !(throwable instanceof WebClientResponseException.BadRequest))
                    .doBeforeRetry(retrySignal -> 
                        log.warn("Retrying fare validation request. Attempt: {}", retrySignal.totalRetries() + 1)
                    )
                )
                .doOnSuccess(response -> 
                    log.info("Successfully validated fare. BookingId: {}, Status: {}", 
                        fareValidateRequest.getBookingId(), response.getStatus())
                )
                .doOnError(error -> 
                    log.error("Error during fare validation: {}", error.getMessage(), error)
                )
                .onErrorMap(throwable -> {
                    if (throwable instanceof FlightServiceException) {
                        return throwable;
                    }
                    return new FlightServiceException(
                        "Unexpected error during fare validation: " + throwable.getMessage(),
                        500
                    );
                });
    }

    @Override
    public Mono<ConfirmBookResponse> confirmBooking(ConfirmBookRequest confirmBookRequest) {
        // Validate request before sending to TripJack
        List<String> validationErrors = confirmBookRequestValidator.validate(confirmBookRequest);
        if (!validationErrors.isEmpty()) {
            String errorMessage = "Confirm booking request validation failed: " + String.join("; ", validationErrors);
            log.error(errorMessage);
            return Mono.error(new FlightServiceException(errorMessage, 400));
        }
        
        // Determine which API to call based on request content
        // If travellerInfo is present, this is an instant book request -> use /air/book
        // If only bookingId and paymentInfos, this is a hold confirm -> use /air/confirm-book
        String apiUrl = confirmBookRequest.isInstantBookRequest() ? bookApiUrl : confirmBookUrl;
        String requestType = confirmBookRequest.isInstantBookRequest() ? "instant book" : "hold confirm";
        
        log.info("Confirming booking for bookingId: {} using {} flow (API: {})", 
                confirmBookRequest.getBookingId(), requestType, apiUrl);
        
        return webClient
                .post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(confirmBookRequest)
                .retrieve()
                .onStatus(
                    HttpStatusCode::is4xxClientError,
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Client error (4xx) occurred during confirm booking: {}", errorBody);
                            return Mono.error(new FlightServiceException(
                                "Client error during confirm booking: " + errorBody,
                                response.statusCode().value()
                            ));
                        })
                )
                .onStatus(
                    HttpStatusCode::is5xxServerError,
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Server error (5xx) occurred during confirm booking: {}", errorBody);
                            return Mono.error(new FlightServiceException(
                                "Server error during confirm booking: " + errorBody,
                                response.statusCode().value()
                            ));
                        })
                )
                .bodyToMono(ConfirmBookResponse.class)
                .timeout(Duration.ofSeconds(60))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(3))
                    .filter(throwable -> !(throwable instanceof FlightServiceException) && !(throwable instanceof WebClientResponseException.BadRequest))
                    .doBeforeRetry(retrySignal -> 
                        log.warn("Retrying confirm booking request. Attempt: {}", retrySignal.totalRetries() + 1)
                    )
                )
                .doOnSuccess(response -> 
                    log.info("Successfully confirmed booking. BookingId: {}, Status: {}", 
                        response.getBookingId(), response.getStatus())
                )
                .doOnError(error -> 
                    log.error("Error during confirm booking: {}", error.getMessage(), error)
                )
                .onErrorMap(throwable -> {
                    if (throwable instanceof FlightServiceException) {
                        return throwable;
                    }
                    return new FlightServiceException(
                        "Unexpected error during confirm booking: " + throwable.getMessage(),
                        500
                    );
                });
    }

    @Override
    public Mono<BookingDetailsResponse> getBookingDetails(BookingDetailsRequest bookingDetailsRequest) {
        log.info("Retrieving booking details for bookingId: {}", bookingDetailsRequest.getBookingId());
        
        return webClient
                .post()
                .uri(bookingDetailsUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(bookingDetailsRequest)
                .retrieve()
                .onStatus(
                    HttpStatusCode::is4xxClientError,
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Client error (4xx) occurred during booking details retrieval: {}", errorBody);
                            return Mono.error(new FlightServiceException(
                                "Client error during booking details retrieval: " + errorBody,
                                response.statusCode().value()
                            ));
                        })
                )
                .onStatus(
                    HttpStatusCode::is5xxServerError,
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Server error (5xx) occurred during booking details retrieval: {}", errorBody);
                            return Mono.error(new FlightServiceException(
                                "Server error during booking details retrieval: " + errorBody,
                                response.statusCode().value()
                            ));
                        })
                )
                .bodyToMono(BookingDetailsResponse.class)
                .timeout(Duration.ofSeconds(30))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(2))
                    .filter(throwable -> !(throwable instanceof FlightServiceException) && !(throwable instanceof WebClientResponseException.BadRequest))
                    .doBeforeRetry(retrySignal -> 
                        log.warn("Retrying booking details request. Attempt: {}", retrySignal.totalRetries() + 1)
                    )
                )
                .doOnSuccess(response -> 
                    log.info("Successfully retrieved booking details. Status: {}", response.getStatus())
                )
                .doOnError(error -> 
                    log.error("Error during booking details retrieval: {}", error.getMessage(), error)
                )
                .onErrorMap(throwable -> {
                    if (throwable instanceof FlightServiceException) {
                        return throwable;
                    }
                    return new FlightServiceException(
                        "Unexpected error during booking details retrieval: " + throwable.getMessage(),
                        500
                    );
                });
    }

    @Override
    public Mono<ReleasePnrResponse> releasePnr(ReleasePnrRequest releasePnrRequest) {
        log.info("Releasing PNR for bookingId: {}, PNRs: {}", 
            releasePnrRequest.getBookingId(), releasePnrRequest.getPnrs());
        
        return webClient
                .post()
                .uri(unholdUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(releasePnrRequest)
                .retrieve()
                .onStatus(
                    HttpStatusCode::is4xxClientError,
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Client error (4xx) occurred during PNR release: {}", errorBody);
                            return Mono.error(new FlightServiceException(
                                "Client error during PNR release: " + errorBody,
                                response.statusCode().value()
                            ));
                        })
                )
                .onStatus(
                    HttpStatusCode::is5xxServerError,
                    response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Server error (5xx) occurred during PNR release: {}", errorBody);
                            return Mono.error(new FlightServiceException(
                                "Server error during PNR release: " + errorBody,
                                response.statusCode().value()
                            ));
                        })
                )
                .bodyToMono(ReleasePnrResponse.class)
                .timeout(Duration.ofSeconds(30))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(2))
                    .filter(throwable -> !(throwable instanceof FlightServiceException) && !(throwable instanceof WebClientResponseException.BadRequest))
                    .doBeforeRetry(retrySignal -> 
                        log.warn("Retrying PNR release request. Attempt: {}", retrySignal.totalRetries() + 1)
                    )
                )
                .doOnSuccess(response -> 
                    log.info("Successfully released PNR. BookingId: {}, Status: {}", 
                        releasePnrRequest.getBookingId(), response.getStatus())
                )
                .doOnError(error -> 
                    log.error("Error during PNR release: {}", error.getMessage(), error)
                )
                .onErrorMap(throwable -> {
                    if (throwable instanceof FlightServiceException) {
                        return throwable;
                    }
                    return new FlightServiceException(
                        "Unexpected error during PNR release: " + throwable.getMessage(),
                        500
                    );
                });
    }
    
    /**
     * Clean incomplete passport info from traveller data.
     * TripJack rejects requests with partial passport info (e.g., pNum without pid).
     * If passport issue date (pid) is missing, remove all passport fields.
     */
    private void cleanIncompletePassportInfo(BookingRequest bookingRequest) {
        if (bookingRequest.getTravellerInfo() == null) {
            return;
        }
        
        for (var traveller : bookingRequest.getTravellerInfo()) {
            // If passport info is present but pid (passport issue date) is missing, clear all passport fields
            boolean hasPassportInfo = traveller.getpNum() != null || traveller.getpNat() != null || traveller.geteD() != null;
            boolean hasPid = traveller.getPid() != null && !traveller.getPid().trim().isEmpty();
            
            if (hasPassportInfo && !hasPid) {
                log.warn("Clearing incomplete passport info for traveller {} {} - missing pid (passport issue date)", 
                        traveller.getfN(), traveller.getlN());
                traveller.setpNum(null);
                traveller.setpNat(null);
                traveller.seteD(null);
                traveller.setPid(null);
                traveller.setDi(null);
            }
        }
    }
    
    /**
     * Send booking confirmation email asynchronously.
     * This method does not block the main booking flow - email failures are logged but don't affect booking.
     * First fetches booking details to get complete flight information.
     */
    private void sendBookingConfirmationEmail(BookingRequest bookingRequest, BookingResponse bookingResponse) {
        try {
            // Check if we have a valid email to send to
            if (bookingRequest.getDeliveryInfo() == null || 
                bookingRequest.getDeliveryInfo().getEmails() == null ||
                bookingRequest.getDeliveryInfo().getEmails().isEmpty()) {
                log.warn("No email address found in booking request, skipping confirmation email");
                return;
            }
            
            String bookingId = bookingResponse.getBookingId();
            
            // Fetch booking details to get complete flight information
            BookingDetailsRequest detailsRequest = new BookingDetailsRequest();
            detailsRequest.setBookingId(bookingId);
            
            getBookingDetails(detailsRequest)
                    .subscribe(
                        detailsResponse -> {
                            try {
                                // Build email with full details from booking-details API
                                BookingConfirmationEmail emailContent = bookingEmailBuilder.buildFromBookingDetails(
                                        bookingRequest, detailsResponse);
                                
                                // Send email
                                emailService.sendBookingConfirmation(emailContent)
                                        .subscribe(
                                            unused -> log.info("Booking confirmation email sent successfully for bookingId: {}", bookingId),
                                            error -> log.error("Failed to send booking confirmation email for bookingId: {}: {}", 
                                                    bookingId, error.getMessage())
                                        );
                            } catch (Exception e) {
                                log.error("Error building email content for bookingId: {}: {}", bookingId, e.getMessage(), e);
                            }
                        },
                        error -> {
                            log.error("Failed to fetch booking details for email, bookingId: {}: {}", 
                                    bookingId, error.getMessage());
                            // Fallback: try to send with limited info from booking response
                            sendEmailWithLimitedInfo(bookingRequest, bookingResponse);
                        }
                    );
                    
        } catch (Exception e) {
            // Log error but don't fail the booking
            log.error("Error preparing booking confirmation email for bookingId: {}: {}", 
                    bookingResponse.getBookingId(), e.getMessage(), e);
        }
    }
    
    /**
     * Fallback method to send email with limited info when booking details fetch fails
     */
    private void sendEmailWithLimitedInfo(BookingRequest bookingRequest, BookingResponse bookingResponse) {
        try {
            BookingConfirmationEmail emailContent = bookingEmailBuilder.buildFromBookingResponse(
                    bookingRequest, bookingResponse);
            
            emailService.sendBookingConfirmation(emailContent)
                    .subscribe(
                        unused -> log.info("Booking confirmation email (limited info) sent for bookingId: {}", 
                                bookingResponse.getBookingId()),
                        error -> log.error("Failed to send booking confirmation email for bookingId: {}: {}", 
                                bookingResponse.getBookingId(), error.getMessage())
                    );
        } catch (Exception e) {
            log.error("Error sending email with limited info for bookingId: {}: {}", 
                    bookingResponse.getBookingId(), e.getMessage(), e);
        }
    }
}
