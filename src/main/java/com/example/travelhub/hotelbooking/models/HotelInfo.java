package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelInfo {

    // Basic Info from TripJack API
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("rt")
    private Integer rt; // Rating
    
    @JsonProperty("ad")
    private Address ad; // Address
    
    @JsonProperty("pt")
    private String pt; // Property type
    
    @JsonProperty("ops")
    private List<HotelOption> ops; // Hotel options with pricing
    
    @JsonProperty("uid")
    private String uid;
    
    @JsonProperty("ifc")
    private Boolean ifc; // Is free cancellation allowed (changed from ifca)
    
    @JsonProperty("gl")
    private GeoLocation gl; // Geo location from TripJack (lt/ln format)
    
    @JsonProperty("imgs")
    private List<ImageInfo> imgs; // Image objects from TripJack
    
    // Additional fields for frontend (not from TripJack API)
    @JsonProperty("images")
    private List<String> images; // Hotel images URLs
    
    @JsonProperty("amenities")
    private List<String> amenities; // Hotel amenities
    
    @JsonProperty("guestRating")
    private Double guestRating; // Guest rating (0-10)
    
    @JsonProperty("reviewCount")
    private Integer reviewCount; // Number of reviews
    
    @JsonProperty("distanceFromCenter")
    private Double distanceFromCenter; // Distance from city center in km
    
    @JsonProperty("coordinates")
    private Coordinates coordinates; // Geo coordinates
    
    @JsonProperty("chain")
    private String chain; // Hotel chain name
    
    @JsonProperty("badges")
    private List<String> badges; // Badges like "Top Pick", "Great Value"
    
    @JsonProperty("stars")
    private Integer stars; // Star rating (1-5)
    
    @JsonProperty("description")
    private String description; // Hotel description
    
    @JsonProperty("checkInTime")
    private CheckInOutTime checkInTime; // Check-in time object
    
    @JsonProperty("checkOutTime")
    private CheckInOutTime checkOutTime; // Check-out time object

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coordinates {
        @JsonProperty("lat")
        private Double lat;
        
        @JsonProperty("lng")
        private Double lng;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CheckInOutTime {
        @JsonProperty("beginTime")
        private String beginTime;
        
        @JsonProperty("endTime")
        private String endTime;
        
        @JsonProperty("minAge")
        private Double minAge;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeoLocation {
        @JsonProperty("lt")
        private String lt; // Latitude
        
        @JsonProperty("ln")
        private String ln; // Longitude
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImageInfo {
        @JsonProperty("url")
        private String url;
        
        @JsonProperty("sz")
        private String sz; // Size (e.g., "XXL")
    }
}