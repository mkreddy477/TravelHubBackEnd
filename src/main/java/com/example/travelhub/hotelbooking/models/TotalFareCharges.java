package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TotalFareCharges {
    
    @JsonProperty("CMU")
    private double CMU;
    
    @JsonProperty("BF")
    private double BF;
    
    @JsonProperty("NF")
    private double NF;
    
    @JsonProperty("TAF")
    private double TAF;
    
    @JsonProperty("TF")
    private double TF;

    // Getters and Setters
    public double getCMU() {
        return CMU;
    }

    public void setCMU(double CMU) {
        this.CMU = CMU;
    }

    public double getBF() {
        return BF;
    }

    public void setBF(double BF) {
        this.BF = BF;
    }

    public double getNF() {
        return NF;
    }

    public void setNF(double NF) {
        this.NF = NF;
    }

    public double getTAF() {
        return TAF;
    }

    public void setTAF(double TAF) {
        this.TAF = TAF;
    }

    public double getTF() {
        return TF;
    }

    public void setTF(double TF) {
        this.TF = TF;
    }
}