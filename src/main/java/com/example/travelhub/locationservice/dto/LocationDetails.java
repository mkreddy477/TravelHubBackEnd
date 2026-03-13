package com.example.travelhub.locationservice.dto;

import com.example.travelhub.locationservice.model.LocationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDetails {
    private String locationId;
    private String name;
    private LocationType type;
    private String cityName;
    private String stateName;
    private String countryCode;
    private String countryName;
    private Integer hotelCount;
}
