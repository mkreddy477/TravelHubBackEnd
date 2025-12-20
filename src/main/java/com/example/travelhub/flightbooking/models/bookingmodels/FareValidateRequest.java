package com.example.travelhub.flightbooking.models.bookingmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request DTO for TripJack Fare Validate API
 * Used to validate if fare for PNR is still available before ticketing
 * URL: https://apitest.tripjack.com/oms/v1/air/fare-validate
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FareValidateRequest {
    
    private String bookingId;

    public FareValidateRequest() {
    }

    public FareValidateRequest(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public String toString() {
        return "FareValidateRequest{bookingId='" + bookingId + "'}";
    }
}
