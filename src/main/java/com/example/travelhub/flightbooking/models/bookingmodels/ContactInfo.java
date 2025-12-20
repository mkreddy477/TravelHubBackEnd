package com.example.travelhub.flightbooking.models.bookingmodels;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Emergency Contact Details Related to Booking (which need to be passed to supplier)
 * emails - Emergency Email Id (It should be valid email id) - which is used by airline
 * contacts - Valid Contact Number (Followed with country code and valid mobile number)
 *            Example (+919500112233) - which is used by airline
 * ecn - Emergency Contact Name - which is used by airline
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContactInfo {
    
    private List<String> emails;
    private List<String> contacts;
    private String ecn; // Emergency Contact Name

    public ContactInfo() {
    }

    public ContactInfo(List<String> emails, List<String> contacts, String ecn) {
        this.emails = emails;
        this.contacts = contacts;
        this.ecn = ecn;
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

    public String getEcn() {
        return ecn;
    }

    public void setEcn(String ecn) {
        this.ecn = ecn;
    }

    @Override
    public String toString() {
        return "ContactInfo{emails=" + emails + ", contacts=" + contacts + ", ecn='" + ecn + "'}";
    }
}
