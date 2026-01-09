package com.example.travelhub.hotelbooking.models;

import java.util.List;

public class Hotel {
    
    private String id;
    private String name;
    private int rt;
    private Address ad;
    private String pt;
    private List<Instruction> inst;
    private List<HotelDetailOption> ops;
    private String uid;
    
    public Hotel() {
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
    
    public List<Instruction> getInst() {
        return inst;
    }
    
    public void setInst(List<Instruction> inst) {
        this.inst = inst;
    }
    
    public List<HotelDetailOption> getOps() {
        return ops;
    }
    
    public void setOps(List<HotelDetailOption> ops) {
        this.ops = ops;
    }
    
    public String getUid() {
        return uid;
    }
    
    public void setUid(String uid) {
        this.uid = uid;
    }
}