package com.example.travelhub.locationservice.repository;

import com.example.travelhub.locationservice.model.HotelDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class HotelDataRepository {
    
    @Value("${hotel.static-data.file.path:classpath:hotel-details.json}")
    private String filePath;
    
    private final ObjectMapper objectMapper;
    private final Map<String, HotelDetails> hotelDetailsMap = new ConcurrentHashMap<>();
    private LocalDateTime lastLoadedAt;
    
    public HotelDataRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @PostConstruct
    public void init() {
        loadHotelData();
    }
    
    public void loadHotelData() {
        log.info("Loading hotel static data from file: {}", filePath);
        try {
            InputStream inputStream;
            
            if (filePath.startsWith("classpath:")) {
                String resourcePath = filePath.substring("classpath:".length());
                Resource resource = new ClassPathResource(resourcePath);
                if (!resource.exists()) {
                    log.warn("Hotel details file not found: {}, starting with empty data", filePath);
                    return;
                }
                inputStream = resource.getInputStream();
            } else {
                Resource resource = new FileSystemResource(filePath);
                if (!resource.exists()) {
                    log.warn("Hotel details file not found: {}, starting with empty data", filePath);
                    return;
                }
                inputStream = resource.getInputStream();
            }
            
            // Assuming the file contains an array of HotelDetails
            HotelDetails[] hotelDetailsArray = objectMapper.readValue(inputStream, HotelDetails[].class);
            
            hotelDetailsMap.clear();
            for (HotelDetails details : hotelDetailsArray) {
                hotelDetailsMap.put(details.getHotelId(), details);
            }
            
            lastLoadedAt = LocalDateTime.now();
            log.info("Successfully loaded {} hotel details", hotelDetailsMap.size());
            
            inputStream.close();
            
        } catch (IOException e) {
            log.error("Error loading hotel static data from file", e);
            // Don't throw exception - allow service to start with empty data
        }
    }
    
    public Optional<HotelDetails> findByHotelId(String hotelId) {
        return Optional.ofNullable(hotelDetailsMap.get(hotelId));
    }
    
    public List<HotelDetails> findByHotelIds(List<String> hotelIds) {
        return hotelIds.stream()
                .map(hotelDetailsMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    public Map<String, HotelDetails> findByHotelIdsAsMap(List<String> hotelIds) {
        return hotelIds.stream()
                .map(hotelDetailsMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(HotelDetails::getHotelId, details -> details));
    }
    
    public int getTotalHotels() {
        return hotelDetailsMap.size();
    }
    
    public LocalDateTime getLastLoadedAt() {
        return lastLoadedAt;
    }
}