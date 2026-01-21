package com.example.travelhub.locationservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationMetadata {
    private String timezone;
    private String currency;
    private String language;
    private Double latitude;
    private Double longitude;
    private String popularityScore;
    
    @Builder.Default
    private Map<String, Object> customAttributes = new HashMap<>();
    
    public void addCustomAttribute(String key, Object value) {
        if (customAttributes == null) {
            customAttributes = new HashMap<>();
        }
        customAttributes.put(key, value);
    }
    
    public Object getCustomAttribute(String key) {
        return customAttributes != null ? customAttributes.get(key) : null;
    }
}
