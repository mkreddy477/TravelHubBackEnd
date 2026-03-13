package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomInfo {
    
	@NotNull
    @Min(1)
    private int numberOfAdults;
	
    private int numberOfChild;
    
    private List<Integer> childAge;
    
    public RoomInfo() {
    }
    
    public int getNumberOfAdults() {
        return numberOfAdults;
    }
    
    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
    }
    
    public int getNumberOfChild() {
        return numberOfChild;
    }
    
    public void setNumberOfChild(int numberOfChild) {
        this.numberOfChild = numberOfChild;
    }
    
    public List<Integer> getChildAge() {
        return childAge;
    }
    
    public void setChildAge(List<Integer> childAge) {
        this.childAge = childAge;
    }
}