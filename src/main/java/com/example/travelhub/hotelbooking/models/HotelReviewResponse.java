package com.example.travelhub.hotelbooking.models;

import com.example.travelhub.hotelbooking.models.HotelDetailResponse.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelReviewResponse {
    
    @JsonProperty("hInfo")
    private HotelInfo hInfo;
    
    @JsonProperty("bookingId")
    private String bookingId;
    
    @JsonProperty("status")
    private Status status;
    
    @JsonProperty("conditions")
    private BookingConditions conditions;
}