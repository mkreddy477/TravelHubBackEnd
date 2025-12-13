package com.example.travelhub.flightbooking.models;

import java.util.List;

public class FlightSearchResultGroup {

    private java.util.List<FlightOptionDto> onward;
    private java.util.List<FlightOptionDto> returns;
    private java.util.List<FlightOptionDto> combos;

    public FlightSearchResultGroup() {
    }

    public FlightSearchResultGroup(java.util.List<FlightOptionDto> onward,
                                   java.util.List<FlightOptionDto> returns,
                                   java.util.List<FlightOptionDto> combos) {
        this.onward = onward;
        this.returns = returns;
        this.combos = combos;
    }

    public java.util.List<FlightOptionDto> getOnward() {
        return onward;
    }

    public void setOnward(java.util.List<FlightOptionDto> onward) {
        this.onward = onward;
    }

    public java.util.List<FlightOptionDto> getReturns() {
        return returns;
    }

    public void setReturns(java.util.List<FlightOptionDto> returns) {
        this.returns = returns;
    }

    public java.util.List<FlightOptionDto> getCombos() {
        return combos;
    }

    public void setCombos(java.util.List<FlightOptionDto> combos) {
        this.combos = combos;
    }
}
