package com.example.travelhub.flightbooking.models.bookingmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request DTO for TripJack Booking Details API
 * Used to retrieve booking details and status of an existing booking
 * URL: https://apitest.tripjack.com/oms/v1/booking-details
 * 
 * bookingId - Id of the booking to retrieve
 * requirePaxPricing - If true, returns traveller level pricing info (Version 1.5+)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDetailsRequest {
    
    private String bookingId;
    private Boolean requirePaxPricing;

    public BookingDetailsRequest() {
    }

    public BookingDetailsRequest(String bookingId) {
        this.bookingId = bookingId;
    }

    public BookingDetailsRequest(String bookingId, Boolean requirePaxPricing) {
        this.bookingId = bookingId;
        this.requirePaxPricing = requirePaxPricing;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Boolean getRequirePaxPricing() {
        return requirePaxPricing;
    }

    public void setRequirePaxPricing(Boolean requirePaxPricing) {
        this.requirePaxPricing = requirePaxPricing;
    }

    @Override
    public String toString() {
        return "BookingDetailsRequest{bookingId='" + bookingId + "', requirePaxPricing=" + requirePaxPricing + '}';
    }
}
