package com.example.travelhub.locationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchLocationResponse {
    private List<LocationDetails> locations;
    private Integer totalCount;
}
