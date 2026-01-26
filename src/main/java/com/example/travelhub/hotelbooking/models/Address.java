package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
    
    @JsonProperty("adr")
    private String adr; // Street address
    
    @JsonProperty("adr2")
    private String adr2; // Additional address info
    
    @JsonProperty("postalCode")
    private String postalCode;
    
    @JsonProperty("ctn")
    private String ctn; // City name (flat field)
    
    @JsonProperty("cn")
    private String cn; // Country name (flat field)
    
    @JsonProperty("city")
    private City city; // City object from TripJack
    
    @JsonProperty("state")
    private State state; // State object from TripJack
    
    @JsonProperty("country")
    private Country country; // Country object from TripJack
    
    @JsonProperty("countryCode")
    private String countryCode; // Country code
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class City {
        @JsonProperty("name")
        private String name;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class State {
        @JsonProperty("name")
        private String name;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Country {
        @JsonProperty("name")
        private String name;
    }
}