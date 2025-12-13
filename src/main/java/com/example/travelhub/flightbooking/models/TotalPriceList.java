package com.example.travelhub.flightbooking.models;

public class TotalPriceList {

    private String id;
    private String fareIdentifier;
    private FareDetail fd;

    public TotalPriceList() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFareIdentifier() {
        return fareIdentifier;
    }

    public void setFareIdentifier(String fareIdentifier) {
        this.fareIdentifier = fareIdentifier;
    }

    public FareDetail getFd() {
        return fd;
    }

    public void setFd(FareDetail fd) {
        this.fd = fd;
    }
}
