package com.example.travelhub.locationservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationHierarchy {
    private String cityId;
    private String cityName;
    private String stateId;
    private String stateName;
    private String countryId;
    private String countryCode;
    private String countryName;
    
    public String getParentId(LocationType childType) {
        switch (childType) {
            case CITY:
                return stateId;
            case STATE:
                return countryId;
            case COUNTRY:
                return null;
            default:
                return null;
        }
    }
    
    public boolean hasParent(LocationType childType) {
        return getParentId(childType) != null;
    }
}
