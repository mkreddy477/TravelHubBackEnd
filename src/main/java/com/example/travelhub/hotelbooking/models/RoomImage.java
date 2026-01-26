package com.example.travelhub.hotelbooking.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomImage {
    
    private String url;
    private String sz;
    private List<String> rids;
    
    public RoomImage() {
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getSz() {
        return sz;
    }
    
    public void setSz(String sz) {
        this.sz = sz;
    }
    
    public List<String> getRids() {
        return rids;
    }
    
    public void setRids(List<String> rids) {
        this.rids = rids;
    }
}