package com.example.travelhub.hotelbooking.models;

public class CancellationPolicyResponse {
    
    private String id;
    private CancellationPolicy cancellationPolicy;
    
    public CancellationPolicyResponse() {
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public CancellationPolicy getCancellationPolicy() {
        return cancellationPolicy;
    }
    
    public void setCancellationPolicy(CancellationPolicy cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }
}