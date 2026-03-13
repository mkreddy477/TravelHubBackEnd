package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingConditions {
    
    @JsonProperty("isBA")
    private boolean isBA;  // Is blocking allowed
    
    @JsonProperty("st")
    private int st;  // Session time in seconds
}