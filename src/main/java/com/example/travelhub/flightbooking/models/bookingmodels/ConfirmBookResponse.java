package com.example.travelhub.flightbooking.models.bookingmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Response DTO for TripJack Confirm Book API
 * Contains confirmation result for hold booking
 * 
 * Cases:
 * 1. Fare/booking class not available on Supplier side for Hold PNR
 * 2. Hold time limit expired for Hold PNR
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfirmBookResponse {
    
    private BookingStatus status;
    private String bookingId;
    private JsonNode itemInfos;
    private JsonNode order;
    private JsonNode errors;

    public ConfirmBookResponse() {
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

    public JsonNode getItemInfos() {
        return itemInfos;
    }

    public void setItemInfos(JsonNode itemInfos) {
        this.itemInfos = itemInfos;
    }

    public JsonNode getOrder() {
        return order;
    }

    public void setOrder(JsonNode order) {
        this.order = order;
    }

    public JsonNode getErrors() {
        return errors;
    }

    public void setErrors(JsonNode errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ConfirmBookResponse{bookingId='" + bookingId + "', status=" + status + '}';
    }
}
