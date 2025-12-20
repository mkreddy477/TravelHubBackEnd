package com.example.travelhub.flightbooking.models.bookingmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Payment information for booking
 * amount - total payable amount to Tripjack for the booking (TF from AirReviewResponse)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentInfo {
    
    private Number amount;

    public PaymentInfo() {
    }

    public PaymentInfo(Number amount) {
        this.amount = amount;
    }

    public Number getAmount() {
        return amount;
    }

    public void setAmount(Number amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PaymentInfo{amount=" + amount + '}';
    }
}
