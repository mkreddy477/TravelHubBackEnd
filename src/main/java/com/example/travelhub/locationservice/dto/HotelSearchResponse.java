package com.example.travelhub.locationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelSearchResponse {
    private String locationId;
    private String locationName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private List<HotelInfo> hotels;
    private Integer totalCount;
    private SearchCriteria searchCriteria;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HotelInfo {
        private String hotelId;
        private String hotelName;
        private Integer rating;
        private String cityName;
        private String stateName;
        private String countryCode;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchCriteria {
        private Integer nights;
        private Integer rooms;
        private Integer guests;
        private Integer minRating;
    }
}
