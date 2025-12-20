package com.example.travelhub.flightbooking.models.bookingmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Passport Expiry Date information
 * month - Expiry month (integer 1-12)
 * year - Expiry year (integer YYYY format)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpiryDate {
    
    private Integer month;
    private Integer year;

    public ExpiryDate() {
    }

    public ExpiryDate(Integer month, Integer year) {
        this.month = month;
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "ExpiryDate{month='" + month + "', year='" + year + "'}";
    }
}
