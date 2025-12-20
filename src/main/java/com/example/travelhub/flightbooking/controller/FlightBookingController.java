package com.example.travelhub.flightbooking.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

/**
 * REST Controller for Flight Booking operations using WebFlux
 * API: https://apitest.tripjack.com/oms/v1/air/book
 */
@RestController
@RequestMapping("/api/v1/flights")
public class FlightBookingController {
    
    private static final Logger log = LoggerFactory.getLogger(FlightBookingController.class);

    private final FlightBookingService flightBookingService;

    public FlightBookingController(FlightBookingService flightBookingService) {
        this.flightBookingService = flightBookingService;
    }

    /**
     * Book a flight with the given booking request
     * 
     * @param bookingRequest containing booking details, traveller info, payment info, etc.
     * @return Mono of ResponseEntity<BookingResponse>
     */
    @PostMapping(
        value = "/book",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<BookingResponse>> bookFlight(
            @Valid @RequestBody BookingRequest bookingRequest) {
        
        log.info("Received flight booking request for bookingId: {}", bookingRequest.getBookingId());
        log.info("Number of travellers: {}", 
                bookingRequest.getTravellerInfo() != null ? bookingRequest.getTravellerInfo().size() : 0);
        
        return flightBookingService.bookFlight(bookingRequest)
                .map(response -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnSuccess(response -> 
                    log.info("Flight booking request completed successfully")
                )
                .doOnError(error -> 
                    log.error("Flight booking request failed: {}", error.getMessage())
                );
    }

    /**
     * Alternative endpoint that returns just the response body (common pattern)
     * 
     * @param bookingRequest containing booking details, traveller info, payment info, etc.
     * @return Mono of BookingResponse
     */
    @PostMapping(
        value = "/book-direct",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Mono<BookingResponse> bookFlightDirect(
            @Valid @RequestBody BookingRequest bookingRequest) {
        
        log.info("Received direct flight booking request for bookingId: {}", bookingRequest.getBookingId());
        
        return flightBookingService.bookFlight(bookingRequest);
    }

    /**
     * Hold a flight booking without payment
     * Request should NOT have paymentInfo - used for Hold & Hold Confirm Ticket flow
     * 
     * @param bookingRequest containing booking details, traveller info (no payment info)
     * @return Mono of ResponseEntity<BookingResponse>
     */
    @PostMapping(
        value = "/hold",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<BookingResponse>> holdBooking(
            @Valid @RequestBody BookingRequest bookingRequest) {
        
        log.info("Received hold booking request for bookingId: {}", bookingRequest.getBookingId());
        
        return flightBookingService.holdBooking(bookingRequest)
                .map(response -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnSuccess(response -> 
                    log.info("Hold booking request completed successfully")
                )
                .doOnError(error -> 
                    log.error("Hold booking request failed: {}", error.getMessage())
                );
    }

 
    @PostMapping(
        value = "/fare-validate",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<FareValidateResponse>> validateFare(
            @Valid @RequestBody FareValidateRequest fareValidateRequest) {
        
        log.info("Received fare validation request for bookingId: {}", fareValidateRequest.getBookingId());
        
        return flightBookingService.validateFare(fareValidateRequest)
                .map(response -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnSuccess(response -> 
                    log.info("Fare validation request completed successfully")
                )
                .doOnError(error -> 
                    log.error("Fare validation request failed: {}", error.getMessage())
                );
    }

    /**
     * Confirm a hold booking by making payment
     * URL: https://apitest.tripjack.com/oms/v1/air/confirm-book
     * 
     * Cases:
     * 1. Fare/booking class not available on Supplier side for Hold PNR
     * 2. Hold time limit expired for Hold PNR
     * 
     * @param confirmBookRequest containing bookingId and paymentInfos
     * @return Mono of ResponseEntity<ConfirmBookResponse>
     */
    @PostMapping(
        value = "/confirm-book",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<ConfirmBookResponse>> confirmBooking(
            @Valid @RequestBody ConfirmBookRequest confirmBookRequest) {
        
        log.info("Received confirm booking request for bookingId: {}", confirmBookRequest.getBookingId());
        
        return flightBookingService.confirmBooking(confirmBookRequest)
                .map(response -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnSuccess(response -> 
                    log.info("Confirm booking request completed successfully")
                )
                .doOnError(error -> 
                    log.error("Confirm booking request failed: {}", error.getMessage())
                );
    }

    /**
     * Retrieve booking details and status of an existing booking
     * URL: https://apitest.tripjack.com/oms/v1/booking-details
     * 
     * Order Status values:
     * - SUCCESS: Order success with Payment & PNR & Ticket Number generated
     * - ON_HOLD: Order has been blocked
     * - CANCELLED: Order has been cancelled
     * - FAILED: Order failed due to other reasons
     * - PENDING: Order in pending state
     * - ABORTED: Order has been aborted
     * - UNCONFIRMED: Hold booking not confirmed and PNR released
     * 
     * @param bookingDetailsRequest containing bookingId and optional requirePaxPricing
     * @return Mono of ResponseEntity<BookingDetailsResponse>
     */
    @PostMapping(
        value = "/booking-details",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<BookingDetailsResponse>> getBookingDetails(
            @Valid @RequestBody BookingDetailsRequest bookingDetailsRequest) {
        
        log.info("Received booking details request for bookingId: {}", bookingDetailsRequest.getBookingId());
        
        return flightBookingService.getBookingDetails(bookingDetailsRequest)
                .map(response -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnSuccess(response -> 
                    log.info("Booking details request completed successfully")
                )
                .doOnError(error -> 
                    log.error("Booking details request failed: {}", error.getMessage())
                );
    }

    /**
     * Release a PNR that is currently on HOLD status
     * URL: https://apitest.tripjack.com/oms/v1/air/unhold
     * 
     * After calling this API, call Booking Details API to confirm
     * Order Status should be UNCONFIRMED to confirm PNR is released
     * 
     * @param releasePnrRequest containing bookingId and pnrs to release
     * @return Mono of ResponseEntity<ReleasePnrResponse>
     */
    @PostMapping(
        value = "/release-pnr",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<ReleasePnrResponse>> releasePnr(
            @Valid @RequestBody ReleasePnrRequest releasePnrRequest) {
        
        log.info("Received release PNR request for bookingId: {}", releasePnrRequest.getBookingId());
        
        return flightBookingService.releasePnr(releasePnrRequest)
                .map(response -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(response))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnSuccess(response -> 
                    log.info("Release PNR request completed successfully")
                )
                .doOnError(error -> 
                    log.error("Release PNR request failed: {}", error.getMessage())
                );
    }
}
