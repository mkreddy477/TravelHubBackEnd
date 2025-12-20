package com.example.travelhub.flightbooking.models.bookingmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * SSR (Special Service Request) Info for baggage, meal, seat, extra services
 * key - Segment Key from AirReviewResponse
 * code - Customer Selected SSR Code on Corresponding Segment
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SsrInfo {
    
    private String key;
    private String code;

    public SsrInfo() {
    }

    public SsrInfo(String key, String code) {
        this.key = key;
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "SsrInfo{key='" + key + "', code='" + code + "'}";
    }
}
