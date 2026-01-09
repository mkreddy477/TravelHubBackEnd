package com.example.travelhub.hotelbooking.models;

public class PenaltyDetail {
    
    private String fdt;
    private String tdt;
    private double am;
    private double pp;
    private int rn;
    
    public PenaltyDetail() {
    }
    
    public String getFdt() {
        return fdt;
    }
    
    public void setFdt(String fdt) {
        this.fdt = fdt;
    }
    
    public String getTdt() {
        return tdt;
    }
    
    public void setTdt(String tdt) {
        this.tdt = tdt;
    }
    
    public double getAm() {
        return am;
    }
    
    public void setAm(double am) {
        this.am = am;
    }
    
    public double getPp() {
        return pp;
    }
    
    public void setPp(double pp) {
        this.pp = pp;
    }
    
    public int getRn() {
        return rn;
    }
    
    public void setRn(int rn) {
        this.rn = rn;
    }
}