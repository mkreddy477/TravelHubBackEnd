package com.example.travelhub.flightbooking.models;

public class AdvancedOptions {

    private boolean directOnly;
    private boolean flexibleDates;
    private String preferredAirline;
    private String baggage;

    public AdvancedOptions() {
    }

    public boolean isDirectOnly() {
        return directOnly;
    }

    public void setDirectOnly(boolean directOnly) {
        this.directOnly = directOnly;
    }

    public boolean isFlexibleDates() {
        return flexibleDates;
    }

    public void setFlexibleDates(boolean flexibleDates) {
        this.flexibleDates = flexibleDates;
    }

    public String getPreferredAirline() {
        return preferredAirline;
    }

    public void setPreferredAirline(String preferredAirline) {
        this.preferredAirline = preferredAirline;
    }

    public String getBaggage() {
        return baggage;
    }

    public void setBaggage(String baggage) {
        this.baggage = baggage;
    }
}
