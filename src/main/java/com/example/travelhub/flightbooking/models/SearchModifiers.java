package com.example.travelhub.flightbooking.models;

public class SearchModifiers {

    private boolean isDirectFlight ;
    private boolean isConnectingFlight;
    private String pft;

    public SearchModifiers() {
    }

    public SearchModifiers(boolean isDirectFlight,
                           boolean isConnectingFlight,
                           String pft) {
        this.isDirectFlight = isDirectFlight;
        this.isConnectingFlight = isConnectingFlight;
        this.pft = pft;
    }

    public boolean getIsDirectFlight() {
        return isDirectFlight;
    }

    public void setIsDirectFlight(boolean isDirectFlight) {
        this.isDirectFlight = isDirectFlight;
    }

    public boolean getIsConnectingFlight() {
        return isConnectingFlight;
    }

    public void setIsConnectingFlight(boolean isConnectingFlight) {
        this.isConnectingFlight = isConnectingFlight;
    }

    public String getPft() {
        return pft;
    }

    public void setPft(String pft) {
        this.pft = pft;
    }
}
