package com.example.travelhub.flightbooking.models;

import java.time.LocalDate;

public class MultiCityLeg {

    private String origin;
    private String destination;
    private LocalDate travelDate;

    public MultiCityLeg() {}

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDate getTravelDate() { return travelDate; }
    public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }
}
