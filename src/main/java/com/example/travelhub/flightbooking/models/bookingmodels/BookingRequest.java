package com.example.travelhub.flightbooking.models.bookingmodels;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request DTO for TripJack Book API
 * bookingId - Booking Id from AirReviewResponse
 * paymentInfos - Payment for the booking to Tripjack (List<PaymentInfo>)
 * deliveryInfo - Delivery Details Related to Booking
 * contactInfo - Emergency Contact Details Related to Booking
 * travellerInfo - List of Traveller which flight to be booked
 * gstInfo - GST Which needs to be passed for billing and supplier
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingRequest {
    
    private String bookingId;
    private List<PaymentInfo> paymentInfos;
    private DeliveryInfo deliveryInfo;
    private ContactInfo contactInfo;
    private List<TravellerInfo> travellerInfo;
    private GstInfo gstInfo;

    public BookingRequest() {
    }

    public BookingRequest(String bookingId, List<PaymentInfo> paymentInfos, DeliveryInfo deliveryInfo,
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

    @Override
    public String toString() {
        return "BookingRequest{" +
                "bookingId='" + bookingId + '\'' +
                ", deliveryInfo=" + deliveryInfo +
                ", contactInfo=" + contactInfo +
                ", travellerInfo=" + travellerInfo +
                ", gstInfo=" + gstInfo +
                '}';
    }
}
