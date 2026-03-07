package com.example.travelhub.locationservice.Util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.example.travelhub.locationservice.model.HotelDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

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
        } else {
            log.warn("Unexpected JSON structure. Root is neither an array nor has 'hotelOpInfos' field");
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
            
            if (hotelId == null) {
                log.warn("Skipping hotel with null ID");
                return null;
            }
            
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
            
            // Add check-in/check-out times if available
            if (hotelNode.has("checkInTime")) {
                metadata.put("checkInTime", getTextValue(hotelNode, "checkInTime"));
            }
            if (hotelNode.has("checkOutTime")) {
                metadata.put("checkOutTime", getTextValue(hotelNode, "checkOutTime"));
            }
            
            // Add guest rating if available
            if (hotelNode.has("guestRating")) {
                metadata.put("guestRating", getDoubleValue(hotelNode, "guestRating"));
            }
            
            // Add review count if available
            if (hotelNode.has("reviewCount")) {
                metadata.put("reviewCount", getIntValue(hotelNode, "reviewCount"));
            }
            
            // Add chain info if available
            if (hotelNode.has("chain")) {
                metadata.put("chain", getTextValue(hotelNode, "chain"));
            }
            
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
            log.error("Error extracting hotel details from node: {}", e.getMessage(), e);
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
                    String url = imageNode.get("url").asText();
                    if (url != null && !url.isEmpty()) {
                        images.add(url);
                    }
                } else if (imageNode.isTextual()) {
                    // Handle case where images is array of strings
                    String url = imageNode.asText();
                    if (url != null && !url.isEmpty()) {
                        images.add(url);
                    }
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
        
        // Try different possible field names
        String[] possibleFields = {"facilities", "amenities", "features"};
        
        for (String fieldName : possibleFields) {
            if (hotelNode.has(fieldName) && hotelNode.get(fieldName).isArray()) {
                for (JsonNode facilityNode : hotelNode.get(fieldName)) {
                    if (facilityNode.has("name")) {
                        String amenity = facilityNode.get("name").asText();
                        if (amenity != null && !amenity.isEmpty()) {
                            amenities.add(amenity);
                        }
                    } else if (facilityNode.isTextual()) {
                        // Handle case where amenities is array of strings
                        String amenity = facilityNode.asText();
                        if (amenity != null && !amenity.isEmpty()) {
                            amenities.add(amenity);
                        }
                    }
                }
            }
        }
        
        return amenities;
    }
    
    /**
     * Extract address from hotel node
     */
    private HotelDetails.Address extractAddress(JsonNode hotelNode) {
        JsonNode addressNode = null;
        
        // Try different possible field names for address
        if (hotelNode.has("address")) {
            addressNode = hotelNode.get("address");
        } else if (hotelNode.has("ad")) {
            addressNode = hotelNode.get("ad");
        }
        
        if (addressNode == null) {
            log.debug("No address found for hotel");
            return null;
        }
        
        // Extract coordinates
        HotelDetails.Coordinates coordinates = null;
        
        // Try geolocation field
        if (hotelNode.has("geolocation")) {
            JsonNode geoNode = hotelNode.get("geolocation");
            Double lat = getDoubleValue(geoNode, "lt");
            Double lng = getDoubleValue(geoNode, "ln");
            
            if (lat != null && lng != null) {
                coordinates = HotelDetails.Coordinates.builder()
                        .latitude(lat)
                        .longitude(lng)
                        .build();
            }
        }
        
        // Try coordinates field directly in address
        if (coordinates == null && addressNode.has("coordinates")) {
            JsonNode coordNode = addressNode.get("coordinates");
            Double lat = getDoubleValue(coordNode, "latitude");
            Double lng = getDoubleValue(coordNode, "longitude");
            
            if (lat != null && lng != null) {
                coordinates = HotelDetails.Coordinates.builder()
                        .latitude(lat)
                        .longitude(lng)
                        .build();
            }
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
        JsonNode contactNode = null;
        
        // Try different possible field names
        if (hotelNode.has("contact")) {
            contactNode = hotelNode.get("contact");
        } else if (hotelNode.has("contactInfo")) {
            contactNode = hotelNode.get("contactInfo");
        }
        
        if (contactNode == null) {
            log.debug("No contact info found for hotel");
            // Return empty contact info instead of null
            return HotelDetails.ContactInfo.builder().build();
        }
        
        // Extract phone
        String phone = getTextValue(contactNode, "ph");
        if (phone == null) {
            phone = getTextValue(contactNode, "phone");
        }
        
        // Extract email
        String email = getTextValue(contactNode, "email");
        if (email == null) {
            email = getTextValue(contactNode, "em");
        }
        
        // Extract website
        String website = getTextValue(contactNode, "website");
        if (website == null) {
            website = getTextValue(contactNode, "web");
        }
        
        return HotelDetails.ContactInfo.builder()
                .phone(phone)
                .email(email)
                .website(website)
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
            
            String result = description.toString().trim();
            return result.isEmpty() ? null : result;
            
        } catch (Exception e) {
            log.debug("Could not parse description as JSON, returning as plain text");
            // Return the original string if it's not JSON
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