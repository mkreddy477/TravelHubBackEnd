package com.example.travelhub.hotelbooking.models;

import java.util.List;

public class CancellationPolicy {
    
    private String id;
    private boolean ifr;
    private boolean inr;
    private List<PenaltyDetail> pd;
    
    public CancellationPolicy() {
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public boolean isIfr() {
        return ifr;
    }
    
    public void setIfr(boolean ifr) {
        this.ifr = ifr;
    }
    
    public boolean isInr() {
        return inr;
    }
    
    public void setInr(boolean inr) {
        this.inr = inr;
    }
    
    public List<PenaltyDetail> getPd() {
        return pd;
    }
    
    public void setPd(List<PenaltyDetail> pd) {
        this.pd = pd;
    }
}