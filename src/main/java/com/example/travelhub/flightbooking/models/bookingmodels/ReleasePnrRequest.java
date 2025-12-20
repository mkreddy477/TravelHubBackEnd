package com.example.travelhub.flightbooking.models.bookingmodels;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Request DTO for TripJack Release PNR (Unhold) API
 * Used to release a PNR that is currently on HOLD status
 * URL: https://apitest.tripjack.com/oms/v1/air/unhold
 * 
 * bookingId - Id of the booking to release
 * pnrs - Array of PNRs that need to be released
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReleasePnrRequest {
    
    private String bookingId;
    private List<String> pnrs;

    public ReleasePnrRequest() {
    }

    public ReleasePnrRequest(String bookingId, List<String> pnrs) {
        this.bookingId = bookingId;
        this.pnrs = pnrs;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public List<String> getPnrs() {
        return pnrs;
    }

    public void setPnrs(List<String> pnrs) {
        this.pnrs = pnrs;
    }

    @Override
    public String toString() {
        return "ReleasePnrRequest{bookingId='" + bookingId + "', pnrs=" + pnrs + '}';
    }
}
