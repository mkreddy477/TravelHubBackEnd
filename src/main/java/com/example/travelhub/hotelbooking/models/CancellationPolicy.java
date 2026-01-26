package com.example.travelhub.hotelbooking.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CancellationPolicy {
    
    private String id;
    
    @JsonProperty("ifra")
    private boolean ifra;  // ✅ Add this field
    
    @JsonProperty("inra")
    private boolean inra;  // ✅ Change from "inr" to "inra"
    
    private List<PolicyDetail> pd;

    // Constructor
    public CancellationPolicy() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIfra() {
        return ifra;
    }

    public void setIfra(boolean ifra) {
        this.ifra = ifra;
    }

    public boolean isInra() {
        return inra;
    }

    public void setInra(boolean inra) {
        this.inra = inra;
    }

    public List<PolicyDetail> getPd() {
        return pd;
    }

    public void setPd(List<PolicyDetail> pd) {
        this.pd = pd;
    }
}