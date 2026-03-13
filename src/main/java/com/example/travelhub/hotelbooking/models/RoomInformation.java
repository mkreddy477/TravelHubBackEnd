package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomInformation {
    
    private String id;
    private String rc;
    private String mb;
    private double tp;
    
    public RoomInformation() {
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getRc() {
        return rc;
    }
    
    public void setRc(String rc) {
        this.rc = rc;
    }
    
    public String getMb() {
        return mb;
    }
    
    public void setMb(String mb) {
        this.mb = mb;
    }
    
    public double getTp() {
        return tp;
    }
    
    public void setTp(double tp) {
        this.tp = tp;
    }
}