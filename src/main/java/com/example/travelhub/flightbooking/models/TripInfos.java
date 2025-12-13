package com.example.travelhub.flightbooking.models;

import java.util.List;

public class TripInfos {

    private java.util.List<TripInfo> onward;
    private java.util.List<TripInfo> returnTrips;
    private java.util.List<TripInfo> combo;

    public TripInfos() {
    }

    public java.util.List<TripInfo> getOnward() {
        return onward;
    }

    public void setOnward(java.util.List<TripInfo> onward) {
        this.onward = onward;
    }

    public java.util.List<TripInfo> getReturnTrips() {
        return returnTrips;
    }

    public void setReturnTrips(java.util.List<TripInfo> returnTrips) {
        this.returnTrips = returnTrips;
    }

    public java.util.List<TripInfo> getCombo() {
        return combo;
    }

    public void setCombo(java.util.List<TripInfo> combo) {
        this.combo = combo;
    }
}
