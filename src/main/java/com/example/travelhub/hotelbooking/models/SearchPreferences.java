package com.example.travelhub.hotelbooking.models;

import java.util.List;

public class SearchPreferences {
    
    private List<Integer> ratings;
    private List<String> hids;
    private boolean fsc;
    
    public SearchPreferences() {
    }
    
    public List<Integer> getRatings() {
        return ratings;
    }
    
    public void setRatings(List<Integer> ratings) {
        this.ratings = ratings;
    }
    
    public List<String> getHids() {
        return hids;
    }
    
    public void setHids(List<String> hids) {
        this.hids = hids;
    }
    
    public boolean isFsc() {
        return fsc;
    }
    
    public void setFsc(boolean fsc) {
        this.fsc = fsc;
    }
}