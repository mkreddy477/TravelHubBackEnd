package com.example.travelhub.flightbooking.models.bookingmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Response DTO for TripJack Fare Validate API
 * Contains validation result for fare availability before ticketing
 * 
 * Cases:
 * 1. Success - Fare & Booking class still valid to confirm
 * 2. Fare/booking class not available on Supplier side
 * 3. Hold time limit expired for Hold PNR
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FareValidateResponse {
    
    private BookingStatus status;
    private String bookingId;
    private JsonNode errors;

    public FareValidateResponse() {
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public JsonNode getErrors() {
        return errors;
    }

    public void setErrors(JsonNode errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "FareValidateResponse{bookingId='" + bookingId + "', status=" + status + '}';
    }
}
