package com.example.travelhub.locationservice.model;

import java.util.Set;

public class Location {
    private String locationId;
    private String name;
    private LocationType type;
    private String cityName;
    private String stateName;
    private String countryCode;
    private String countryName;
    private Set<String> hotelIds;
    private Integer hotelCount;
    
    private String parentLocationId;
    private LocationHierarchy hierarchy;
    private LocationMetadata metadata;
    
    // Constructors
    public Location() {
    }
    
    public Location(String locationId, String name, LocationType type, 
                   String cityName, String stateName, String countryCode, 
                   String countryName, Set<String> hotelIds, Integer hotelCount,
                   String parentLocationId, LocationHierarchy hierarchy, 
                   LocationMetadata metadata) {
        this.locationId = locationId;
        this.name = name;
        this.type = type;
        this.cityName = cityName;
        this.stateName = stateName;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.hotelIds = hotelIds;
        this.hotelCount = hotelCount;
        this.parentLocationId = parentLocationId;
        this.hierarchy = hierarchy;
        this.metadata = metadata;
    }
    
    // Getters
    public String getLocationId() {
        return locationId;
    }
    
    public String getName() {
        return name;
    }
    
    public LocationType getType() {
        return type;
    }
    
    public String getCityName() {
        return cityName;
    }
    
    public String getStateName() {
        return stateName;
    }
    
    public String getCountryCode() {
        return countryCode;
    }
    
    public String getCountryName() {
        return countryName;
    }
    
    public Set<String> getHotelIds() {
        return hotelIds;
    }
    
    public Integer getHotelCount() {
        return hotelCount;
    }
    
    public String getParentLocationId() {
        return parentLocationId;
    }
    
    public LocationHierarchy getHierarchy() {
        return hierarchy;
    }
    
    public LocationMetadata getMetadata() {
        return metadata;
    }
    
    // Setters
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setType(LocationType type) {
        this.type = type;
    }
    
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
    
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    
    public void setHotelIds(Set<String> hotelIds) {
        this.hotelIds = hotelIds;
    }
    
    public void setHotelCount(Integer hotelCount) {
        this.hotelCount = hotelCount;
    }
    
    public void setParentLocationId(String parentLocationId) {
        this.parentLocationId = parentLocationId;
    }
    
    public void setHierarchy(LocationHierarchy hierarchy) {
        this.hierarchy = hierarchy;
    }
    
    public void setMetadata(LocationMetadata metadata) {
        this.metadata = metadata;
    }
    
    // Custom methods (keep these as they have business logic)
    public String getParentId() {
        if (hierarchy != null) {
            return hierarchy.getParentId(type);
        }
        return parentLocationId;
    }
    
    public boolean hasParent() {
        return getParentId() != null;
    }
    
    // Builder pattern (manual implementation)
    public static LocationBuilder builder() {
        return new LocationBuilder();
    }
    
    public static class LocationBuilder {
        private String locationId;
        private String name;
        private LocationType type;
        private String cityName;
        private String stateName;
        private String countryCode;
        private String countryName;
        private Set<String> hotelIds;
        private Integer hotelCount;
        private String parentLocationId;
        private LocationHierarchy hierarchy;
        private LocationMetadata metadata;
        
        public LocationBuilder locationId(String locationId) {
            this.locationId = locationId;
            return this;
        }
        
        public LocationBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        public LocationBuilder type(LocationType type) {
            this.type = type;
            return this;
        }
        
        public LocationBuilder cityName(String cityName) {
            this.cityName = cityName;
            return this;
        }
        
        public LocationBuilder stateName(String stateName) {
            this.stateName = stateName;
            return this;
        }
        
        public LocationBuilder countryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }
        
        public LocationBuilder countryName(String countryName) {
            this.countryName = countryName;
            return this;
        }
        
        public LocationBuilder hotelIds(Set<String> hotelIds) {
            this.hotelIds = hotelIds;
            return this;
        }
        
        public LocationBuilder hotelCount(Integer hotelCount) {
            this.hotelCount = hotelCount;
            return this;
        }
        
        public LocationBuilder parentLocationId(String parentLocationId) {
            this.parentLocationId = parentLocationId;
            return this;
        }
        
        public LocationBuilder hierarchy(LocationHierarchy hierarchy) {
            this.hierarchy = hierarchy;
            return this;
        }
        
        public LocationBuilder metadata(LocationMetadata metadata) {
            this.metadata = metadata;
            return this;
        }
        
        public Location build() {
            return new Location(locationId, name, type, cityName, stateName, 
                              countryCode, countryName, hotelIds, hotelCount,
                              parentLocationId, hierarchy, metadata);
        }
    }
    
    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return locationId != null && locationId.equals(location.locationId);
    }
    
    @Override
    public int hashCode() {
        return locationId != null ? locationId.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Location{" +
                "locationId='" + locationId + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", cityName='" + cityName + '\'' +
                ", stateName='" + stateName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                ", hotelIds=" + hotelIds +
                ", hotelCount=" + hotelCount +
                ", parentLocationId='" + parentLocationId + '\'' +
                ", hierarchy=" + hierarchy +
                ", metadata=" + metadata +
                '}';
    }
}