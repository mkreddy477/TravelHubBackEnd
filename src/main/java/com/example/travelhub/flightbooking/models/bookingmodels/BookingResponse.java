package com.example.travelhub.flightbooking.models.bookingmodels;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Response DTO for TripJack Book API
 * Contains booking confirmation details including bookingId and status
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingResponse {
    
    private String bookingId;
    private BookingStatus status;
    private JsonNode itemInfos;
    private JsonNode searchQuery;
    private JsonNode gstInfo;
    private JsonNode deliveryInfo;
    private List<JsonNode> alertList;
    private JsonNode order;
    private JsonNode errors;

    public BookingResponse() {
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public JsonNode getItemInfos() {
        return itemInfos;
    }

    public void setItemInfos(JsonNode itemInfos) {
        this.itemInfos = itemInfos;
    }

    public JsonNode getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(JsonNode searchQuery) {
        this.searchQuery = searchQuery;
    }

    public JsonNode getGstInfo() {
        return gstInfo;
    }

    public void setGstInfo(JsonNode gstInfo) {
        this.gstInfo = gstInfo;
    }

    public JsonNode getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(JsonNode deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public List<JsonNode> getAlertList() {
        return alertList;
    }

    public void setAlertList(List<JsonNode> alertList) {
        this.alertList = alertList;
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
        return "BookingResponse{bookingId='" + bookingId + "', status=" + status + '}';
    }
}
