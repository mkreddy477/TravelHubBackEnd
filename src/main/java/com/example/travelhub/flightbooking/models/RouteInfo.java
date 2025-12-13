package com.example.travelhub.flightbooking.models;

public class RouteInfo {

    private CityOrAirport fromCityOrAirport;
    private CityOrAirport toCityOrAirport;
    private String travelDate;

    public RouteInfo() {
    }

    public RouteInfo(CityOrAirport fromCityOrAirport,
                     CityOrAirport toCityOrAirport,
                     String travelDate) {
        this.fromCityOrAirport = fromCityOrAirport;
        this.toCityOrAirport = toCityOrAirport;
        this.travelDate = travelDate;
    }

    public CityOrAirport getFromCityOrAirport() {
        return fromCityOrAirport;
    }

    public void setFromCityOrAirport(CityOrAirport fromCityOrAirport) {
        this.fromCityOrAirport = fromCityOrAirport;
    }

    public CityOrAirport getToCityOrAirport() {
        return toCityOrAirport;
    }

    public void setToCityOrAirport(CityOrAirport toCityOrAirport) {
        this.toCityOrAirport = toCityOrAirport;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }
}
