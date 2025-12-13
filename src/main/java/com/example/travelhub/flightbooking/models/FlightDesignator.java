package com.example.travelhub.flightbooking.models;

public class FlightDesignator {

    private AirlineInfo airlineInfo;
    private String flightNumber;

    public FlightDesignator() {
    }

    public AirlineInfo getAirlineInfo() {
        return airlineInfo;
    }

    public void setAirlineInfo(AirlineInfo airlineInfo) {
        this.airlineInfo = airlineInfo;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }
}
