package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TotalAdjustedFareCharges {
    
    @JsonProperty("TAF")
    private TaxAndFee TAF;

    public TaxAndFee getTAF() {
        return TAF;
    }

    public void setTAF(TaxAndFee TAF) {
        this.TAF = TAF;
    }
}