package com.example.travelhub.locationservice.dto;

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
public class TripJackSearchRequest {
    
    @JsonProperty("searchQuery")
    private SearchQuery searchQuery;
    
    @JsonProperty("sync")
    private Boolean sync;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchQuery {
        @JsonProperty("checkinDate")
        private String checkinDate;  // Format: "YYYY-MM-DD"
        
        @JsonProperty("checkoutDate")
        private String checkoutDate; // Format: "YYYY-MM-DD"
        
        @JsonProperty("roomInfo")
        private List<RoomInfo> roomInfo;
        
        @JsonProperty("searchCriteria")
        private SearchCriteria searchCriteria;
        
        @JsonProperty("searchPreferences")
        private SearchPreferences searchPreferences;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomInfo {
        @JsonProperty("numberOfAdults")
        private Integer numberOfAdults;
        
        @JsonProperty("numberOfChild")
        private Integer numberOfChild;
        
        @JsonProperty("childAge")
        private List<Integer> childAge;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchCriteria {
        @JsonProperty("nationality")
        private String nationality; // e.g., "US"
        
        @JsonProperty("currency")
        private String currency;    // e.g., "USD"
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchPreferences {
        @JsonProperty("ratings")
        private List<Integer> ratings;
        
        @JsonProperty("hids")
        private List<String> hids; // Hotel IDs to search (max 100)
        
        @JsonProperty("fsc")
        private Boolean fsc;
    }
}