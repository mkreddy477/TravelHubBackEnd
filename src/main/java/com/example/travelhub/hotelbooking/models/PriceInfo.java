package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceInfo {
    
    private int day;
    
    @JsonProperty("fc")
    private TotalFareCharges fc;
    
    @JsonProperty("afc")
    private TotalAdjustedFareCharges afc;

    // Getters and Setters
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public TotalFareCharges getFc() {
        return fc;
    }

    public void setFc(TotalFareCharges fc) {
        this.fc = fc;
    }

    public TotalAdjustedFareCharges getAfc() {
        return afc;
    }

    public void setAfc(TotalAdjustedFareCharges afc) {
        this.afc = afc;
    }
}