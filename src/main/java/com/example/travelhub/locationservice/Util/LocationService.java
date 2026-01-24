package com.example.travelhub.locationservice.util;

import com.example.travelhub.locationservice.model.HotelDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HotelDataExtractor {
    
    private final ObjectMapper objectMapper;
    
    public HotelDataExtractor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    /**
     * Extract hotel details from HotelSearchallApi.txt and save to hotel-details.json
     */
    public void extractAndSaveHotelDetails() throws IOException {
        log.info("Starting hotel details extraction from HotelSearchallApi.txt...");
        
        // Read the source file
        ClassPathResource resource = new ClassPathResource("HotelSearchallApi.txt");
        InputStream inputStream = resource.getInputStream();
        
        // Parse as JsonNode to handle the structure
        JsonNode rootNode = objectMapper.readTree(inputStream);
        
        List<HotelDetails> hotelDetailsList = new ArrayList<>();
        
        // Check if it's an array or has a specific structure
        if (rootNode.isArray()) {
            for (JsonNode hotelNode : rootNode) {
                HotelDetails details = extractHotelDetails(hotelNode);
                if (details != null) {
                    hotelDetailsList.add(details);
                }
            }
        } else if (rootNode.has("hotelOpInfos")) {
            // If wrapped in an object with hotelOpInfos array
            JsonNode hotelsArray = rootNode.get("hotelOpInfos");
            for (JsonNode hotelNode : hotelsArray) {
                HotelDetails details = extractHotelDetails(hotelNode);
                if (details != null) {
                    hotelDetailsList.add(details);
                }
            }
        }
        
        // Save to hotel-details.json
        String outputJson = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(hotelDetailsList);
        
        Files.writeString(
            Paths.get("src/main/resources/hotel-details.json"),
            outputJson
        );
        
        log.info("Successfully extracted {} hotel details to hotel-details.json", 
                hotelDetailsList.size());
        
        inputStream.close();
    }
    
    /**
     * Extract hotel details from a single hotel JSON node
     */
    private HotelDetails extractHotelDetails(JsonNode hotelNode) {
        try {
            // Extract basic info
            String hotelId = getTextValue(hotelNode, "hotelId");
            String name = getTextValue(hotelNode, "name");
            Integer rating = getIntValue(hotelNode, "rating");
            String propertyType = getTextValue(hotelNode, "propertyType");
            
            // Extract description (it's a JSON string, so we'll parse it)
            String descriptionJson = getTextValue(hotelNode, "description");
            String cleanDescription = extractCleanDescription(descriptionJson);
            
            // Extract images
            List<String> images = extractImages(hotelNode);
            
            // Extract facilities/amenities
            List<String> amenities = extractAmenities(hotelNode);
            
            // Extract address
            HotelDetails.Address address = extractAddress(hotelNode);
            
            // Extract contact info
            HotelDetails.ContactInfo contactInfo = extractContactInfo(hotelNode);
            
            // Build metadata
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("propertyType", propertyType);
            metadata.put("unicaId", getTextValue(hotelNode, "unicaId"));
            metadata.put("isDeleted", getBooleanValue(hotelNode, "isDeleted"));
            metadata.put("createdOn", getTextValue(hotelNode, "createdOn"));
            
            return HotelDetails.builder()
                    .hotelId(hotelId)
                    .name(name)
                    .description(cleanDescription)
                    .images(images)
                    .starRating(rating)
                    .amenities(amenities)
                    .address(address)
                    .contactInfo(contactInfo)
                    .metadata(metadata)
                    .build();
                    
        } catch (Exception e) {
            log.error("Error extracting hotel details from node", e);
            return null;
        }
    }
    
    /**
     * Extract images from hotel node
     */
    private List<String> extractImages(JsonNode hotelNode) {
        List<String> images = new ArrayList<>();
        
        if (hotelNode.has("images") && hotelNode.get("images").isArray()) {
            for (JsonNode imageNode : hotelNode.get("images")) {
                if (imageNode.has("url")) {
                    images.add(imageNode.get("url").asText());
                }
            }
        }
        
        return images;
    }
    
    /**
     * Extract amenities/facilities from hotel node
     */
    private List<String> extractAmenities(JsonNode hotelNode) {
        List<String> amenities = new ArrayList<>();
        
        if (hotelNode.has("facilities") && hotelNode.get("facilities").isArray()) {
            for (JsonNode facilityNode : hotelNode.get("facilities")) {
                if (facilityNode.has("name")) {
                    amenities.add(facilityNode.get("name").asText());
                }
            }
        }
        
        return amenities;
    }
    
    /**
     * Extract address from hotel node
     */
    private HotelDetails.Address extractAddress(JsonNode hotelNode) {
        if (!hotelNode.has("address")) {
            return null;
        }
        
        JsonNode addressNode = hotelNode.get("address");
        
        // Extract coordinates
        HotelDetails.Coordinates coordinates = null;
        if (hotelNode.has("geolocation")) {
            JsonNode geoNode = hotelNode.get("geolocation");
            coordinates = HotelDetails.Coordinates.builder()
                    .latitude(getDoubleValue(geoNode, "lt"))
                    .longitude(getDoubleValue(geoNode, "ln"))
                    .build();
        }
        
        return HotelDetails.Address.builder()
                .street(getTextValue(addressNode, "adr"))
                .city(getNestedTextValue(addressNode, "city", "name"))
                .state(getNestedTextValue(addressNode, "state", "name"))
                .country(getNestedTextValue(addressNode, "country", "name"))
                .postalCode(getTextValue(addressNode, "postalCode"))
                .coordinates(coordinates)
                .build();
    }
    
    /**
     * Extract contact info from hotel node
     */
    private HotelDetails.ContactInfo extractContactInfo(JsonNode hotelNode) {
        if (!hotelNode.has("contact")) {
            return null;
        }
        
        JsonNode contactNode = hotelNode.get("contact");
        
        return HotelDetails.ContactInfo.builder()
                .phone(getTextValue(contactNode, "ph"))
                .email(null) // Not in the sample data
                .build();
    }
    
    /**
     * Extract clean description from JSON string
     */
    private String extractCleanDescription(String descriptionJson) {
        if (descriptionJson == null || descriptionJson.isEmpty()) {
            return null;
        }
        
        try {
            // Parse the description JSON
            JsonNode descNode = objectMapper.readTree(descriptionJson);
            
            // Combine relevant parts
            StringBuilder description = new StringBuilder();
            
            if (descNode.has("headline")) {
                description.append(descNode.get("headline").asText()).append(". ");
            }
            
            if (descNode.has("location")) {
                description.append(descNode.get("location").asText()).append(" ");
            }
            
            if (descNode.has("amenities")) {
                description.append(descNode.get("amenities").asText());
            }
            
            return description.toString().trim();
            
        } catch (Exception e) {
            log.warn("Could not parse description JSON, returning as-is");
            return descriptionJson;
        }
    }
    
    // Helper methods
    private String getTextValue(JsonNode node, String fieldName) {
        return node.has(fieldName) && !node.get(fieldName).isNull() 
                ? node.get(fieldName).asText() 
                : null;
    }
    
    private String getNestedTextValue(JsonNode node, String parent, String child) {
        if (node.has(parent) && !node.get(parent).isNull()) {
            JsonNode parentNode = node.get(parent);
            if (parentNode.has(child) && !parentNode.get(child).isNull()) {
                return parentNode.get(child).asText();
            }
        }
        return null;
    }
    
    private Integer getIntValue(JsonNode node, String fieldName) {
        return node.has(fieldName) && !node.get(fieldName).isNull() 
                ? node.get(fieldName).asInt() 
                : null;
    }
    
    private Double getDoubleValue(JsonNode node, String fieldName) {
        return node.has(fieldName) && !node.get(fieldName).isNull() 
                ? node.get(fieldName).asDouble() 
                : null;
    }
    
    private Boolean getBooleanValue(JsonNode node, String fieldName) {
        return node.has(fieldName) && !node.get(fieldName).isNull() 
                ? node.get(fieldName).asBoolean() 
                : null;
    }
}