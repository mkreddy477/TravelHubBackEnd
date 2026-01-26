package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelReviewRequest {
    
    @JsonProperty("hotelId")
    private String hotelId;
    
    @JsonProperty("optionId")
    private String optionId;
}