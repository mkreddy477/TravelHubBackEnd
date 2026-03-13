package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HotelOperationRequest {
    
    @JsonProperty("id")
    @JsonAlias("hotelId")  // Accepts both "id" and "hotelId" from JSON
    private String id;
    
    private String optionId;
    
    @JsonProperty("checkinDate")
    @JsonAlias("checkInDate")  // Accepts both formats from frontend
    private String checkInDate;
    
    @JsonProperty("checkoutDate")
    @JsonAlias("checkOutDate")  // Accepts both formats from frontend
    private String checkOutDate;
    
    public HotelOperationRequest() {
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getOptionId() {
        return optionId;
    }
    
    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }
    
    public String getCheckInDate() {
        return checkInDate;
    }
    
    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }
    
    public String getCheckOutDate() {
        return checkOutDate;
    }
    
    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
}