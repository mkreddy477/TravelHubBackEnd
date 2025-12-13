package com.example.travelhub.flightbooking.models;

import java.time.LocalDate;
import java.util.List;

public class FlightSearchRequest {

    private String tripType;
    private String origin;
    private String destination;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private Passengers passengers;
    private String travelClass;
    private AdvancedOptions advancedOptions;
    private List<MultiCityLeg> multiCityLegs;

    public FlightSearchRequest() {
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Passengers getPassengers() {
        return passengers;
    }

    public void setPassengers(Passengers passengers) {
        this.passengers = passengers;
    }

    public String getTravelClass() {
        return travelClass;
    }

    public void setTravelClass(String travelClass) {
        this.travelClass = travelClass;
    }

    public AdvancedOptions getAdvancedOptions() {
        return advancedOptions;
    }

    public void setAdvancedOptions(AdvancedOptions advancedOptions) {
        this.advancedOptions = advancedOptions;
    }

    public List<MultiCityLeg> getMultiCityLegs() {
        return multiCityLegs;
    }

    public void setMultiCityLegs(List<MultiCityLeg> multiCityLegs) {
        this.multiCityLegs = multiCityLegs;
    }
}
