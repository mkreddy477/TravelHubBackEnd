package com.example.travelhub.flightbooking.models.bookingmodels;

import java.util.List;

 import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingRequest {
    
    private String bookingId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonAlias({"base_amount", "BASE_AMOUNT", "baseAmount"})
    private java.math.BigDecimal baseAmount;
    @JsonProperty("fareDetails")
    private FareDetails fareDetails;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonAlias({"booking_detail_id", "BOOKING_DETAIL_ID"})
    private Long bookingDetailId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonAlias({"username", "userName"})
    private String createdBy;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonAlias({"payment_method_id", "PAYMENT_METHOD_ID"})
    private Integer paymentMethodId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonAlias({"payment_status_id", "PAYMENT_STATUS_ID"})
    private Integer paymentStatusId;
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

    public java.math.BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(java.math.BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public FareDetails getFareDetails() {
        return fareDetails;
    }

    public void setFareDetails(FareDetails fareDetails) {
        this.fareDetails = fareDetails;
    }

    public Long getBookingDetailId() {
        return bookingDetailId;
    }

    public void setBookingDetailId(Long bookingDetailId) {
        this.bookingDetailId = bookingDetailId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Integer getPaymentStatusId() {
        return paymentStatusId;
    }

    public void setPaymentStatusId(Integer paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
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
                ", paymentInfos=" + paymentInfos +
                ", deliveryInfo=" + deliveryInfo +
                ", contactInfo=" + contactInfo +
                ", travellerInfo=" + travellerInfo +
                ", fareDetails=" + fareDetails +
                '}';
    }
}
