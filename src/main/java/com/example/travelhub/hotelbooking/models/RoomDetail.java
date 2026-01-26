package com.example.travelhub.hotelbooking.models;

import java.util.ArrayList;
import java.util.List;

import com.example.travelhub.flightbooking.service.TripjackResponse.PriceInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomDetail {

    private String id;
    private String rc;
    private String rt;
    private String srn;
    private int adt;
    private int chd;
    
    @JsonProperty("pdes")
    private List<Object> pdes = new ArrayList<>();
    
    private String mb;
    private double tp;
    
    // Add missing fields
    @JsonProperty("tfcs")
    private TotalFareCharges tfcs;
    
    @JsonProperty("tafcs")
    private TotalAdjustedFareCharges tafcs;
    
    @JsonProperty("pis")
    private List<PriceInfo> pis = new ArrayList<>();
    
    private List<String> fcs;
    private List<RoomImage> imgs;
    
    @JsonProperty("irrh")
    private boolean irrh;
    
    private RoomAdditionalInfo radi;
    private String op;
    
    @JsonProperty("rexb")
    private RoomExtraBenefits rexb;

    // Constructor
    public RoomDetail() {
    }

    // Getters and Setters
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

    public List<Object> getPdes() {
        return pdes;
    }

    public void setPdes(List<Object> pdes) {
        this.pdes = pdes;
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

    public TotalFareCharges getTfcs() {
        return tfcs;
    }

    public void setTfcs(TotalFareCharges tfcs) {
        this.tfcs = tfcs;
    }

    public TotalAdjustedFareCharges getTafcs() {
        return tafcs;
    }

    public void setTafcs(TotalAdjustedFareCharges tafcs) {
        this.tafcs = tafcs;
    }

    public List<PriceInfo> getPis() {
        return pis;
    }

    public void setPis(List<PriceInfo> pis) {
        this.pis = pis;
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

    public boolean isIrrh() {
        return irrh;
    }

    public void setIrrh(boolean irrh) {
        this.irrh = irrh;
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

    public RoomExtraBenefits getRexb() {
        return rexb;
    }

    public void setRexb(RoomExtraBenefits rexb) {
        this.rexb = rexb;
    }
}