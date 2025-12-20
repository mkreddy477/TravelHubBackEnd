package com.example.travelhub.flightbooking.models.bookingmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Response DTO for TripJack Release PNR (Unhold) API
 * After calling this API, call Booking Details API to confirm
 * Order Status should be UNCONFIRMED to confirm PNR is released
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReleasePnrResponse {
    
    private BookingStatus status;
    private String bookingId;
    private JsonNode errors;

    public ReleasePnrResponse() {
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
        return "ReleasePnrResponse{bookingId='" + bookingId + "', status=" + status + '}';
    }
}
