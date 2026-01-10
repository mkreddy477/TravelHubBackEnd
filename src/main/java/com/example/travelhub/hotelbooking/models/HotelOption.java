package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelOption {
    
    private List<RoomInformation> ris;
    private String id;
    private double tp;
    
    public HotelOption() {
    }
    
    public List<RoomInformation> getRis() {
        return ris;
    }
    
    public void setRis(List<RoomInformation> ris) {
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
}