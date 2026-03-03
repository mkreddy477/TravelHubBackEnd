package com.example.travelhub.flightbooking.models.bookingmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Response DTO for TripJack Booking Details API
 * Contains detailed booking information including PNR, ticket numbers, etc.
 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDetailsResponse {
    
    private BookingStatus status;
    private JsonNode order;
    private JsonNode itemInfos;
    private JsonNode gstInfo;
    private JsonNode errors;

    public BookingDetailsResponse() {
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public JsonNode getOrder() {
        return order;
    }

    public void setOrder(JsonNode order) {
        this.order = order;
    }

    public JsonNode getItemInfos() {
        return itemInfos;
    }

    public void setItemInfos(JsonNode itemInfos) {
        this.itemInfos = itemInfos;
    }

    public JsonNode getGstInfo() {
        return gstInfo;
    }

    public void setGstInfo(JsonNode gstInfo) {
        this.gstInfo = gstInfo;
    }

    public JsonNode getErrors() {
        return errors;
    }

    public void setErrors(JsonNode errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "BookingDetailsResponse{order=" + order + ", status=" + status + '}';
    }
}
