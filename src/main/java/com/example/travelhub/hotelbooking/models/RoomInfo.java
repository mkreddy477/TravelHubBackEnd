package com.example.travelhub.hotelbooking.models;

import java.util.List;

public class RoomInfo {
    
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