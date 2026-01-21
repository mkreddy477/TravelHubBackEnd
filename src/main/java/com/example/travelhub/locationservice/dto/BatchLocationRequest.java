package com.example.travelhub.locationservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchLocationRequest {
    @NotEmpty(message = "Location IDs cannot be empty")
    private List<String> locationIds;
}
