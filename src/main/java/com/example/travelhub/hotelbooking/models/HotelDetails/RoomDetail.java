package com.example.travelhub.hotelbooking.models;

import java.util.List;

public class RoomDetail {
    
    private String id;
    private String rc;
    private String rt;
    private String srn;
    private int adt;
    private int chd;
    private String des;
    private String mb;
    private double tp;
    private List<String> fcs;
    private List<RoomImage> imgs;
    private RoomAdditionalInfo radi;
    private String op;
    private String rexbmap;
    
    public RoomDetail() {
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
    
    public String getRt() {
        return rt;
    }
    
    public void setRt(String rt) {
        this.rt = rt;
    }
    
    public String getSrn() {
        return srn;
    }
    
    public void setSrn(String srn) {
        this.srn = srn;
    }
    
    public int getAdt() {
        return adt;
    }
    
    public void setAdt(int adt) {
        this.adt = adt;
    }
    
    public int getChd() {
        return chd;
    }
    
    public void setChd(int chd) {
        this.chd = chd;
    }
    
    public String getDes() {
        return des;
    }
    
    public void setDes(String des) {
        this.des = des;
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
    
    public List<String> getFcs() {
        return fcs;
    }
    
    public void setFcs(List<String> fcs) {
        this.fcs = fcs;
    }
    
    public List<RoomImage> getImgs() {
        return imgs;
    }
    
    public void setImgs(List<RoomImage> imgs) {
        this.imgs = imgs;
    }
    
    public RoomAdditionalInfo getRadi() {
        return radi;
    }
    
    public void setRadi(RoomAdditionalInfo radi) {
        this.radi = radi;
    }
    
    public String getOp() {
        return op;
    }
    
    public void setOp(String op) {
        this.op = op;
    }
    
    public String getRexbmap() {
        return rexbmap;
    }
    
    public void setRexbmap(String rexbmap) {
        this.rexbmap = rexbmap;
    }
}