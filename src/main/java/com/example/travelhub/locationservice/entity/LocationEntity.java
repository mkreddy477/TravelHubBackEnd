package com.example.travelhub.locationservice.entity;

import com.example.travelhub.locationservice.model.LocationType;
import jakarta.persistence.*;

@Entity
@Table(name = "locations", indexes = {
    @Index(name = "idx_location_name", columnList = "name"),
    @Index(name = "idx_location_type", columnList = "type"),
    @Index(name = "idx_city_state", columnList = "city_name, state_name")
})
public class LocationEntity {
    
    @Id
    @Column(name = "location_id", length = 100)
    private String locationId;
    
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private LocationType type;
    
    @Column(name = "city_name", length = 200)
    private String cityName;
    
    @Column(name = "state_name", length = 200)
    private String stateName;
    
    @Column(name = "country_code", length = 10)
    private String countryCode;
    
    @Column(name = "country_name", length = 200)
    private String countryName;
    
    @Column(name = "hotel_count")
    private Integer hotelCount;
    
    // Constructors
    public LocationEntity() {
    }
    
    public LocationEntity(String locationId, String name, LocationType type, 
                         String cityName, String stateName, String countryCode, 
                         String countryName, Integer hotelCount) {
        this.locationId = locationId;
        this.name = name;
        this.type = type;
        this.cityName = cityName;
        this.stateName = stateName;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.hotelCount = hotelCount;
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
    
    public Integer getHotelCount() {
        return hotelCount;
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
    
    public void setHotelCount(Integer hotelCount) {
        this.hotelCount = hotelCount;
    }
    
    // Builder pattern (manual implementation)
    public static LocationEntityBuilder builder() {
        return new LocationEntityBuilder();
    }
    
    public static class LocationEntityBuilder {
        private String locationId;
        private String name;
        private LocationType type;
        private String cityName;
        private String stateName;
        private String countryCode;
        private String countryName;
        private Integer hotelCount;
        
        public LocationEntityBuilder locationId(String locationId) {
            this.locationId = locationId;
            return this;
        }
        
        public LocationEntityBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        public LocationEntityBuilder type(LocationType type) {
            this.type = type;
            return this;
        }
        
        public LocationEntityBuilder cityName(String cityName) {
            this.cityName = cityName;
            return this;
        }
        
        public LocationEntityBuilder stateName(String stateName) {
            this.stateName = stateName;
            return this;
        }
        
        public LocationEntityBuilder countryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }
        
        public LocationEntityBuilder countryName(String countryName) {
            this.countryName = countryName;
            return this;
        }
        
        public LocationEntityBuilder hotelCount(Integer hotelCount) {
            this.hotelCount = hotelCount;
            return this;
        }
        
        public LocationEntity build() {
            return new LocationEntity(locationId, name, type, cityName, 
                                    stateName, countryCode, countryName, hotelCount);
        }
    }
    
    // equals, hashCode, toString (optional but recommended for entities)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationEntity that = (LocationEntity) o;
        return locationId != null && locationId.equals(that.locationId);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "LocationEntity{" +
                "locationId='" + locationId + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", cityName='" + cityName + '\'' +
                ", stateName='" + stateName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                ", hotelCount=" + hotelCount +
                '}';
    }
}