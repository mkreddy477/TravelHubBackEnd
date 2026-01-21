package com.example.travelhub.locationservice.dto;

import com.example.travelhub.locationservice.model.LocationType;
import java.util.List;
import java.util.Map;

public class LocationHotelsResponse {
    private String locationId;
    private String locationName;
    private LocationType locationType;
    private List<String> hotelIds;
    private Integer totalCount;
    private Map<String, Object> appliedFilters;
    
    // Constructors
    public LocationHotelsResponse() {
    }
    
    public LocationHotelsResponse(String locationId, String locationName, 
                                 LocationType locationType, List<String> hotelIds,
                                 Integer totalCount, Map<String, Object> appliedFilters) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationType = locationType;
        this.hotelIds = hotelIds;
        this.totalCount = totalCount;
        this.appliedFilters = appliedFilters;
    }
    
    // Getters
    public String getLocationId() {
        return locationId;
    }
    
    public String getLocationName() {
        return locationName;
    }
    
    public LocationType getLocationType() {
        return locationType;
    }
    
    public List<String> getHotelIds() {
        return hotelIds;
    }
    
    public Integer getTotalCount() {
        return totalCount;
    }
    
    public Map<String, Object> getAppliedFilters() {
        return appliedFilters;
    }
    
    // Setters
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    
    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }
    
    public void setHotelIds(List<String> hotelIds) {
        this.hotelIds = hotelIds;
    }
    
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
    
    public void setAppliedFilters(Map<String, Object> appliedFilters) {
        this.appliedFilters = appliedFilters;
    }
    
    // Builder pattern
    public static LocationHotelsResponseBuilder builder() {
        return new LocationHotelsResponseBuilder();
    }
    
    public static class LocationHotelsResponseBuilder {
        private String locationId;
        private String locationName;
        private LocationType locationType;
        private List<String> hotelIds;
        private Integer totalCount;
        private Map<String, Object> appliedFilters;
        
        public LocationHotelsResponseBuilder locationId(String locationId) {
            this.locationId = locationId;
            return this;
        }
        
        public LocationHotelsResponseBuilder locationName(String locationName) {
            this.locationName = locationName;
            return this;
        }
        
        public LocationHotelsResponseBuilder locationType(LocationType locationType) {
            this.locationType = locationType;
            return this;
        }
        
        public LocationHotelsResponseBuilder hotelIds(List<String> hotelIds) {
            this.hotelIds = hotelIds;
            return this;
        }
        
        public LocationHotelsResponseBuilder totalCount(Integer totalCount) {
            this.totalCount = totalCount;
            return this;
        }
        
        public LocationHotelsResponseBuilder appliedFilters(Map<String, Object> appliedFilters) {
            this.appliedFilters = appliedFilters;
            return this;
        }
        
        public LocationHotelsResponse build() {
            return new LocationHotelsResponse(locationId, locationName, locationType,
                                             hotelIds, totalCount, appliedFilters);
        }
    }
    
    // toString, equals, hashCode
    @Override
    public String toString() {
        return "LocationHotelsResponse{" +
                "locationId='" + locationId + '\'' +
                ", locationName='" + locationName + '\'' +
                ", locationType=" + locationType +
                ", hotelIds=" + hotelIds +
                ", totalCount=" + totalCount +
                ", appliedFilters=" + appliedFilters +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationHotelsResponse that = (LocationHotelsResponse) o;
        return locationId != null && locationId.equals(that.locationId);
    }
    
    @Override
    public int hashCode() {
        return locationId != null ? locationId.hashCode() : 0;
    }
}