package com.example.travelhub.hotelbooking.models;

public class SearchCriteria {
    
    private String nationality;
    private String currency;
    
    public SearchCriteria() {
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}