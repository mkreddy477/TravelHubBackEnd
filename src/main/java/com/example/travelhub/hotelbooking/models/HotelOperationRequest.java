package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HotelOperationRequest {
    
    @JsonProperty("id")
    @JsonAlias("hotelId")  // Accepts both "id" and "hotelId" from JSON
    private String id;
    
    private String optionId;
    
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
}