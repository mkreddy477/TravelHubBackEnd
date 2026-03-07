package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelBookingResponse {
    
    @JsonProperty("bookingId")
    private String bookingId;
    
    @JsonProperty("status")
    private Status status;
    
    @JsonProperty("metaInfo")
    private Map<String, Object> metaInfo;
    
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