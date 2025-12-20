package com.example.travelhub.flightbooking.models.bookingmodels;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request DTO for TripJack Confirm Book API
 * Used to confirm hold booking by making payment OR for instant booking
 * URL: https://apitest.tripjack.com/oms/v1/air/confirm-book (hold confirm)
 * URL: https://apitest.tripjack.com/oms/v1/air/book (instant book with traveller info)
 * 
 * bookingId - Booking id generated at Review
 * paymentInfos - Payment details for the booking (amount is mandatory)
 * deliveryInfo - Delivery Details Related to Booking (for instant book)
 * contactInfo - Emergency Contact Details Related to Booking (for instant book)
 * travellerInfo - List of Traveller which flight to be booked (for instant book)
 * gstInfo - GST Which needs to be passed for billing and supplier (optional)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfirmBookRequest {
    
    private String bookingId;
    private List<PaymentInfo> paymentInfos;
    private DeliveryInfo deliveryInfo;
    private ContactInfo contactInfo;
    private List<TravellerInfo> travellerInfo;
    private GstInfo gstInfo;

    public ConfirmBookRequest() {
    }

    public ConfirmBookRequest(String bookingId, List<PaymentInfo> paymentInfos) {
        this.bookingId = bookingId;
        this.paymentInfos = paymentInfos;
    }

    public ConfirmBookRequest(String bookingId, List<PaymentInfo> paymentInfos, DeliveryInfo deliveryInfo,
                              ContactInfo contactInfo, List<TravellerInfo> travellerInfo, GstInfo gstInfo) {
        this.bookingId = bookingId;
        this.paymentInfos = paymentInfos;
        this.deliveryInfo = deliveryInfo;
        this.contactInfo = contactInfo;
        this.travellerInfo = travellerInfo;
        this.gstInfo = gstInfo;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public List<PaymentInfo> getPaymentInfos() {
        return paymentInfos;
    }

    public void setPaymentInfos(List<PaymentInfo> paymentInfos) {
        this.paymentInfos = paymentInfos;
    }

    public DeliveryInfo getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<TravellerInfo> getTravellerInfo() {
        return travellerInfo;
    }

    public void setTravellerInfo(List<TravellerInfo> travellerInfo) {
        this.travellerInfo = travellerInfo;
    }

    public GstInfo getGstInfo() {
        return gstInfo;
    }

    public void setGstInfo(GstInfo gstInfo) {
        this.gstInfo = gstInfo;
    }

    /**
     * Check if this is an instant book request (has traveller info)
     * vs a hold confirm request (only bookingId and paymentInfos)
     */
    public boolean isInstantBookRequest() {
        return travellerInfo != null && !travellerInfo.isEmpty();
    }

    @Override
    public String toString() {
        return "ConfirmBookRequest{bookingId='" + bookingId + "', paymentInfos=" + paymentInfos + 
               ", travellerInfo=" + travellerInfo + '}';
    }
}
