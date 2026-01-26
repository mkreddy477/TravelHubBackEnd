package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomExtraBenefits {
    
    @JsonProperty("BENEFIT")
    private List<Map<String, List<String>>> BENEFIT;

    public List<Map<String, List<String>>> getBENEFIT() {
        return BENEFIT;
    }

    public void setBENEFIT(List<Map<String, List<String>>> BENEFIT) {
        this.BENEFIT = BENEFIT;
    }
}