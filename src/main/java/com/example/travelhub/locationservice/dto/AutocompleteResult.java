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
public class AutocompleteResult {
    private String locationId;
    private LocationType type;
    private String displayName;
    private String cityName;
    private String stateName;
    private String countryCode;
    private String countryName;
    private Integer hotelCount;
}
