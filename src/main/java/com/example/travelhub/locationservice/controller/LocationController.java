package com.example.travelhub.locationservice.controller;

import com.example.travelhub.locationservice.dto.*;
import com.example.travelhub.locationservice.service.LocationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@RestController
@RequestMapping("/api/hotels")
public class LocationController {

    private final LocationService locationService;
    private static final Logger log = LoggerFactory.getLogger(LocationController.class);

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/autocomplete")
    public ResponseEntity<AutocompleteResponse> autocomplete(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        
        log.info("Autocomplete request - query: {}, limit: {}", query, limit);
        
        if (limit <= 0 || limit > 50) {
            limit = 10;
        }
        
        AutocompleteResponse response = locationService.autocomplete(query, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/locations/{locationId}/hotels")
    public ResponseEntity<LocationHotelsResponse> getLocationHotels(
            @PathVariable String locationId,
            @RequestParam(required = false) Integer minRating) {
        
        log.info("Get hotels request - locationId: {}, minRating: {}", locationId, minRating);
        
        try {
            LocationHotelsResponse response = locationService.getHotelsByLocation(locationId, minRating);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error getting hotels for location: {}", locationId, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/locations/batch")
    public ResponseEntity<BatchLocationResponse> getBatchLocations(
            @Valid @RequestBody BatchLocationRequest request) {
        
        log.info("Batch location request - {} location IDs", request.getLocationIds().size());
        
        BatchLocationResponse response = locationService.getBatchLocations(request.getLocationIds());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<LocationSuggestionsResponse> getLocationSuggestions(
            @RequestParam String query,
            @RequestParam(defaultValue = "10") int limit) {
        
        log.info("Location suggestions request - query: {}, limit: {}", query, limit);
        
        if (limit <= 0 || limit > 50) {
            limit = 10;
        }
        
        LocationSuggestionsResponse response = locationService.getLocationSuggestions(query, limit);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/IDsearch")
    public ResponseEntity<HotelSearchResponse> searchHotels(
            @Valid @RequestBody HotelSearchRequest request) {
        
        log.info("Hotel search request - location: {}, check-in: {}, check-out: {}, rooms: {}", 
                request.getLocationId(), 
                request.getCheckInDate(), 
                request.getCheckOutDate(),
                request.getRooms().size());
        
        try {
            HotelSearchResponse response = locationService.searchHotels(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error searching hotels", e);
            return ResponseEntity.notFound().build();
        }
    }
}
