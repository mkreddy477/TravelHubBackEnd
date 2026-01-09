package com.example.travelhub.hotelbooking.models;

import java.util.List;

public class RoomAdditionalInfo {
    
    private String rid;
    private int mga;
    private int maa;
    private int mca;
    private List<String> vi;
    private List<BedInfo> bds;
    private RoomArea ar;
    
    public RoomAdditionalInfo() {
    }
    
    public String getRid() {
        return rid;
    }
    
    public void setRid(String rid) {
        this.rid = rid;
    }
    
    public int getMga() {
        return mga;
    }
    
    public void setMga(int mga) {
        this.mga = mga;
    }
    
    public int getMaa() {
        return maa;
    }
    
    public void setMaa(int maa) {
        this.maa = maa;
    }
    
    public int getMca() {
        return mca;
    }
    
    public void setMca(int mca) {
        this.mca = mca;
    }
    
    public List<String> getVi() {
        return vi;
    }
    
    public void setVi(List<String> vi) {
        this.vi = vi;
    }
    
    public List<BedInfo> getBds() {
        return bds;
    }
    
    public void setBds(List<BedInfo> bds) {
        this.bds = bds;
    }
    
    public RoomArea getAr() {
        return ar;
    }
    
    public void setAr(RoomArea ar) {
        this.ar = ar;
    }
}