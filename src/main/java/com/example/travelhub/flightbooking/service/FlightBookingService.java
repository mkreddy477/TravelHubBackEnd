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


@Service
public interface FlightBookingService {
    
    
    Mono<BookingResponse> bookFlight(BookingRequest bookingRequest);

    
    Mono<BookingResponse> holdBooking(BookingRequest bookingRequest);

   
    Mono<FareValidateResponse> validateFare(FareValidateRequest fareValidateRequest);

   
    Mono<ConfirmBookResponse> confirmBooking(ConfirmBookRequest confirmBookRequest);

   
    Mono<BookingDetailsResponse> getBookingDetails(BookingDetailsRequest bookingDetailsRequest);

   
    Mono<ReleasePnrResponse> releasePnr(ReleasePnrRequest releasePnrRequest);
}
