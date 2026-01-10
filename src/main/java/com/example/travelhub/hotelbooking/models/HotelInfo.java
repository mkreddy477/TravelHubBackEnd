package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelInfo {
    
    private String id;
    private String name;
    private int rt;
    private Address ad;
    private String pt;
    private List<HotelOption> ops;
    private String uid;
    private boolean ifca;
    
    public HotelInfo() {
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getRt() {
        return rt;
    }
    
    public void setRt(int rt) {
        this.rt = rt;
    }
    
    public Address getAd() {
        return ad;
    }
    
    public void setAd(Address ad) {
        this.ad = ad;
    }
    
    public String getPt() {
        return pt;
    }
    
    public void setPt(String pt) {
        this.pt = pt;
    }
    
    public List<HotelOption> getOps() {
        return ops;
    }
    
    public void setOps(List<HotelOption> ops) {
        this.ops = ops;
    }
    
    public String getUid() {
        return uid;
    }
    
    public void setUid(String uid) {
        this.uid = uid;
    }
    
    public boolean isIfca() {
        return ifca;
    }
    
    public void setIfca(boolean ifca) {
        this.ifca = ifca;
    }
}