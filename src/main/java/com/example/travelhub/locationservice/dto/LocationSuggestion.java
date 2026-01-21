package com.example.travelhub.locationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationSuggestion {
    private String id;
    private String name;
    private String type;
    private String subtitle;
    private String cityName;
    private String stateName;
    private String countryName;
    private String countryCode;
    private Integer hotelCount;
    private Double latitude;
    private Double longitude;
}
