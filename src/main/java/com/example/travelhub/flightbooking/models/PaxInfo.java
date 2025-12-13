package com.example.travelhub.flightbooking.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaxInfo {

    @JsonProperty("ADULT")
    private String adult;

    @JsonProperty("CHILD")
    private String child;

    @JsonProperty("INFANT")
    private String infant;

    public PaxInfo() {
    }

    public PaxInfo(String adult, String child, String infant) {
        this.adult = adult;
        this.child = child;
        this.infant = infant;
    }

    public String getAdult() {
        return adult;
    }
    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getChild() {
        return child;
    }
    public void setChild(String child) {
        this.child = child;
    }

    public String getInfant() {
        return infant;
    }
    public void setInfant(String infant) {
        this.infant = infant;
    }
}
