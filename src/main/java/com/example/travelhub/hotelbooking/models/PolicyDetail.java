package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyDetail {
    
    @JsonProperty("fdt")
    private String fdt;  // From datetime
    
    @JsonProperty("tdt")
    private String tdt;  // To datetime
    
    @JsonProperty("am")
    private double am;   // Amount
    
    @JsonProperty("pp")
    private Double pp;   // Percentage (optional, sometimes present)
    
    @JsonProperty("rn")
    private Integer rn;  // Room number (optional, sometimes present)

    // Constructor
    public PolicyDetail() {
    }

    // Getters and Setters
    public String getFdt() {
        return fdt;
    }

    public void setFdt(String fdt) {
        this.fdt = fdt;
    }

    public String getTdt() {
        return tdt;
    }

    public void setTdt(String tdt) {
        this.tdt = tdt;
    }

    public double getAm() {
        return am;
    }

    public void setAm(double am) {
        this.am = am;
    }

    public Double getPp() {
        return pp;
    }

    public void setPp(Double pp) {
        this.pp = pp;
    }

    public Integer getRn() {
        return rn;
    }

    public void setRn(Integer rn) {
        this.rn = rn;
    }
}