package com.example.travelhub.flightbooking.models;

public class SegmentInfo {

    private String dt;
    private String at;
    private Integer duration;
    private Integer stops;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private FlightDesignator flightDesignator;

    public SegmentInfo() {
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getStops() {
        return stops;
    }

    public void setStops(Integer stops) {
        this.stops = stops;
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public FlightDesignator getFlightDesignator() {
        return flightDesignator;
    }

    public void setFlightDesignator(FlightDesignator flightDesignator) {
        this.flightDesignator = flightDesignator;
    }
}
