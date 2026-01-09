package com.example.travelhub.hotelbooking.models;

import java.util.List;

public class HotelDetailOption {
    
    private List<RoomDetail> ris;
    private String id;
    private double tp;
    private CancellationPolicy cnp;
    private String ddt;
    private List<Instruction> inst;
    private boolean ispr;
    private boolean ipr;
    private boolean ipm;
    
    public HotelDetailOption() {
    }
    
    public List<RoomDetail> getRis() {
        return ris;
    }
    
    public void setRis(List<RoomDetail> ris) {
        this.ris = ris;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public double getTp() {
        return tp;
    }
    
    public void setTp(double tp) {
        this.tp = tp;
    }
    
    public CancellationPolicy getCnp() {
        return cnp;
    }
    
    public void setCnp(CancellationPolicy cnp) {
        this.cnp = cnp;
    }
    
    public String getDdt() {
        return ddt;
    }
    
    public void setDdt(String ddt) {
        this.ddt = ddt;
    }
    
    public List<Instruction> getInst() {
        return inst;
    }
    
    public void setInst(List<Instruction> inst) {
        this.inst = inst;
    }
    
    public boolean isIspr() {
        return ispr;
    }
    
    public void setIspr(boolean ispr) {
        this.ispr = ispr;
    }
    
    public boolean isIpr() {
        return ipr;
    }
    
    public void setIpr(boolean ipr) {
        this.ipr = ipr;
    }
    
    public boolean isIpm() {
        return ipm;
    }
    
    public void setIpm(boolean ipm) {
        this.ipm = ipm;
    }
}