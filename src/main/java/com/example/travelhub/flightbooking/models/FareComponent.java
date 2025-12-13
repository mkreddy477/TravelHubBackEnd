package com.example.travelhub.flightbooking.models;

import java.math.BigDecimal;
import java.util.Map;

public class FareComponent {

    private java.util.Map<String, java.math.BigDecimal> fC;
    private Integer sR;
    private BaggageInfo bI;
    private Integer rT;
    private String cc;
    private String cB;
    private String fB;

    public FareComponent() {
    }

    public java.util.Map<String, java.math.BigDecimal> getfC() {
        return fC;
    }

    public void setfC(java.util.Map<String, java.math.BigDecimal> fC) {
        this.fC = fC;
    }

    public Integer getsR() {
        return sR;
    }

    public void setsR(Integer sR) {
        this.sR = sR;
    }

    public BaggageInfo getbI() {
        return bI;
    }

    public void setbI(BaggageInfo bI) {
        this.bI = bI;
    }

    public Integer getrT() {
        return rT;
    }

    public void setrT(Integer rT) {
        this.rT = rT;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getcB() {
        return cB;
    }

    public void setcB(String cB) {
        this.cB = cB;
    }

    public String getfB() {
        return fB;
    }

    public void setfB(String fB) {
        this.fB = fB;
    }
}
