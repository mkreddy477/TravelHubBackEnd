package com.example.travelhub.flightbooking.models.bookingmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FareDetails {

    @JsonProperty("fareType")
    private String fareType;
    
    @JsonProperty("baseFare")
    private Double baseFare;
    
    @JsonProperty("taxes")
    private Double taxes;
    
    @JsonProperty("total")
    private Double total;

    public FareDetails() {}

    public String getFareType() {
        return fareType;
    }

    public void setFareType(String fareType) {
        this.fareType = fareType;
    }

    public Double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }

    public Double getTaxes() {
        return taxes;
    }

    public void setTaxes(Double taxes) {
        this.taxes = taxes;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "FareDetails{fareType='" + fareType + "', baseFare=" + baseFare + ", taxes=" + taxes + ", total=" + total + '}';
    }
}
