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
public class HotelSearchResponse {
    
    @JsonProperty("searchQuery")
    private SearchQuery searchQuery;
    
    @JsonProperty("searchIds")
    private List<String> searchIds;
    
    @JsonProperty("searchResult")
    private SearchResult searchResult;
    
    @JsonProperty("ruleApplied")
    private Integer ruleApplied;
    
    @JsonProperty("status")
    private Status status;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchQuery {
        @JsonProperty("checkinDate")
        private String checkinDate;
        
        @JsonProperty("checkoutDate")
        private String checkoutDate;
        
        // ... other fields
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchResult {
        @JsonProperty("his")
        private List<HotelInfo> his;
        
        @JsonProperty("size")
        private Integer size;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status {
        @JsonProperty("success")
        private Boolean success;
        
        @JsonProperty("httpStatus")
        private Integer httpStatus;
    }
}