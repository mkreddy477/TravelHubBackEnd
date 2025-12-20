package com.example.travelhub.flightbooking.service;

import org.springframework.stereotype.Service;

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

import reactor.core.publisher.Mono;

/**
 * Service interface for TripJack Flight Booking operations
 */
@Service
public interface FlightBookingService {
    
    /**
     * Book a flight with the given booking request (Instant Book with PaymentInfo)
     * 
     * @param bookingRequest containing booking details, traveller info, payment info, etc.
     * @return Mono of BookingResponse
     */
    Mono<BookingResponse> bookFlight(BookingRequest bookingRequest);

    /**
     * Hold a flight booking without payment (Hold Booking)
     * Request should NOT have paymentInfo
     * 
     * @param bookingRequest containing booking details, traveller info (no payment info)
     * @return Mono of BookingResponse with bookingId and status
     */
    Mono<BookingResponse> holdBooking(BookingRequest bookingRequest);

    /**
     * Validate fare before ticketing for a hold booking
     * Checks if fare is still available for the PNR
     * URL: https://apitest.tripjack.com/oms/v1/air/fare-validate
     * 
     * @param fareValidateRequest containing bookingId
     * @return Mono of FareValidateResponse
     */
    Mono<FareValidateResponse> validateFare(FareValidateRequest fareValidateRequest);

    /**
     * Confirm a hold booking by making payment
     * URL: https://apitest.tripjack.com/oms/v1/air/confirm-book
     * 
     * @param confirmBookRequest containing bookingId and paymentInfos
     * @return Mono of ConfirmBookResponse
     */
    Mono<ConfirmBookResponse> confirmBooking(ConfirmBookRequest confirmBookRequest);

    /**
     * Retrieve booking details and status of an existing booking
     * URL: https://apitest.tripjack.com/oms/v1/booking-details
     * 
     * @param bookingDetailsRequest containing bookingId and optional requirePaxPricing
     * @return Mono of BookingDetailsResponse
     */
    Mono<BookingDetailsResponse> getBookingDetails(BookingDetailsRequest bookingDetailsRequest);

    /**
     * Release a PNR that is currently on HOLD status
     * URL: https://apitest.tripjack.com/oms/v1/air/unhold
     * 
     * @param releasePnrRequest containing bookingId and pnrs to release
     * @return Mono of ReleasePnrResponse
     */
    Mono<ReleasePnrResponse> releasePnr(ReleasePnrRequest releasePnrRequest);
}
