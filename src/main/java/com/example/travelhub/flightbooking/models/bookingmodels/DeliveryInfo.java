package com.example.travelhub.flightbooking.models.bookingmodels;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Delivery Details Related to Booking (which need to be passed to supplier)
 * emails - Booking Email Id (It should be valid email id)
 * contacts - Valid Contact Number (Followed with country code and valid mobile number)
 *            Example (+919500112233)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryInfo {
    
    private List<String> emails;
    private List<String> contacts;

    public DeliveryInfo() {
    }

    public DeliveryInfo(List<String> emails, List<String> contacts) {
        this.emails = emails;
        this.contacts = contacts;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<String> getContacts() {
        return contacts;
    }

    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "DeliveryInfo{emails=" + emails + ", contacts=" + contacts + '}';
    }
}
