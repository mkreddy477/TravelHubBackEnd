package com.example.travelhub.flightbooking.models.reviewmodels;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Request DTO for TripJack Review API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewRequest {
    
    private List<String> priceIds;

    // ✅ DEFAULT CONSTRUCTOR - THIS IS WHAT'S MISSING!
    public ReviewRequest() {
    }

    // Constructor with parameters (optional, but useful)
    public ReviewRequest(List<String> priceIds) {
        this.priceIds = priceIds;
    }

    // ✅ GETTER - Jackson needs this to read the field
    public List<String> getPriceIds() {
        return priceIds;
    }

    // ✅ SETTER - Jackson needs this to write the field
    public void setPriceIds(List<String> priceIds) {
        this.priceIds = priceIds;
    }

    @Override
    public String toString() {
        return "ReviewRequest{priceIds=" + priceIds + '}';
    }
}