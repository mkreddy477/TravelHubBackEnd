package com.example.travelhub.locationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceStats {
    private String dataSource;
    private Integer totalLocations;
    private Integer totalCities;
    private Integer totalStates;
    private Integer totalHotels;
    private LocalDateTime lastLoadedAt;
    private String status;
}
