package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxAndFee {
    
    @JsonProperty("SAC")
    private double SAC;
    
    @JsonProperty("SNP")
    private double SNP;
    
    @JsonProperty("SBP")
    private double SBP;
    
    @JsonProperty("TMF")
    private double TMF;
    
    @JsonProperty("SRC")
    private double SRC;
    
    @JsonProperty("TSF")
    private double TSF;
    
    @JsonProperty("SGP")
    private double SGP;
    
    @JsonProperty("TTSF")
    private double TTSF;

    // Getters and Setters
    public double getSAC() {
        return SAC;
    }

    public void setSAC(double SAC) {
        this.SAC = SAC;
    }

    public double getSNP() {
        return SNP;
    }

    public void setSNP(double SNP) {
        this.SNP = SNP;
    }

    public double getSBP() {
        return SBP;
    }

    public void setSBP(double SBP) {
        this.SBP = SBP;
    }

    public double getTMF() {
        return TMF;
    }

    public void setTMF(double TMF) {
        this.TMF = TMF;
    }

    public double getSRC() {
        return SRC;
    }

    public void setSRC(double SRC) {
        this.SRC = SRC;
    }

    public double getTSF() {
        return TSF;
    }

    public void setTSF(double TSF) {
        this.TSF = TSF;
    }

    public double getSGP() {
        return SGP;
    }

    public void setSGP(double SGP) {
        this.SGP = SGP;
    }

    public double getTTSF() {
        return TTSF;
    }

    public void setTTSF(double TTSF) {
        this.TTSF = TTSF;
    }
}