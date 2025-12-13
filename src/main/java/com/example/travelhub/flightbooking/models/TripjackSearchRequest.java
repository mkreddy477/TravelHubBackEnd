package com.example.travelhub.flightbooking.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripjackSearchRequest {

    private TripjackSearchQuery searchQuery;

    public TripjackSearchRequest() {
    }

    public TripjackSearchRequest(TripjackSearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    public TripjackSearchQuery getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(TripjackSearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }
}
