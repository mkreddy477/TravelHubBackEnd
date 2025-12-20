package com.example.travelhub.flightbooking.models.bookingmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * GST Information for billing and supplier - all fields under gst are mandatory
 * gstNumber - Valid 15 digit GST Number (mandatory)
 * registeredName - GST Registered Name (maximum 35 characters as IATA standard) (mandatory)
 * email - GST Email
 * mobile - GST Mobile
 * address - GST Registered Address (maximum 70 characters as IATA standard)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GstInfo {
    
    private String gstNumber;
    private String registeredName;
    private String email;
    private String mobile;
    private String address;

    public GstInfo() {
    }

    public GstInfo(String gstNumber, String registeredName, String email, String mobile, String address) {
        this.gstNumber = gstNumber;
        this.registeredName = registeredName;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getRegisteredName() {
        return registeredName;
    }

    public void setRegisteredName(String registeredName) {
        this.registeredName = registeredName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "GstInfo{gstNumber='" + gstNumber + "', registeredName='" + registeredName + "'}";
    }
}
