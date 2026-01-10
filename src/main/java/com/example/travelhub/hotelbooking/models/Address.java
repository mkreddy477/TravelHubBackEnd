package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
    
    private String adr;
    private String adr2;
    private String postalCode;
    private String ctn;
    private String cn;
    
    public Address() {
    }
    
    public String getAdr() {
        return adr;
    }
    
    public void setAdr(String adr) {
        this.adr = adr;
    }
    
    public String getAdr2() {
        return adr2;
    }
    
    public void setAdr2(String adr2) {
        this.adr2 = adr2;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getCtn() {
        return ctn;
    }
    
    public void setCtn(String ctn) {
        this.ctn = ctn;
    }
    
    public String getCn() {
        return cn;
    }
    
    public void setCn(String cn) {
        this.cn = cn;
    }
}