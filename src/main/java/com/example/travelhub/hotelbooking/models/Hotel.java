package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hotel {

    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("rt")
    private Integer rt;
    
    @JsonProperty("ad")
    private Address ad;
    
    @JsonProperty("pt")
    private String pt;
    
    @JsonProperty("inst")
    private List<Instruction> inst;
    
    @JsonProperty("ops")
    private List<HotelDetailOption> ops;
    
    @JsonProperty("uid")
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

    public Integer getRt() {
        return rt;
    }

    public void setRt(Integer rt) {
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