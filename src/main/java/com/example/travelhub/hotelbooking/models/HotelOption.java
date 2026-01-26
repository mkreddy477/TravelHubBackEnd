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
public class HotelOption {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("tp")
    private Double tp; // Total price
    
    @JsonProperty("ris")
    private List<RoomInfo> ris; // Room information
    
    @JsonProperty("currency")
    private String currency; // Currency code
    
    @JsonProperty("taxesIncluded")
    private Boolean taxesIncluded;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RoomInfo {
        
        @JsonProperty("id")
        private String id;
        
        @JsonProperty("rc")
        private String rc; // Room category
        
        @JsonProperty("mb")
        private String mb; // Meal basis
        
        @JsonProperty("tp")
        private Double tp; // Total price for this room
        
        @JsonProperty("beds")
        private String beds; // Bed configuration
        
        @JsonProperty("size")
        private Integer size; // Room size in sq meters
        
        @JsonProperty("maxGuests")
        private Integer maxGuests;
        
        @JsonProperty("amenities")
        private List<String> amenities; // Room-specific amenities
    }
}