package com.example.travelhub.locationservice.service;

import com.example.travelhub.locationservice.dto.*;
import com.example.travelhub.locationservice.model.Location;
import com.example.travelhub.locationservice.model.LocationType;
import com.example.travelhub.locationservice.repository.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public AutocompleteResponse autocomplete(String query, int limit) {
        log.debug("Autocomplete search for query: {}, limit: {}", query, limit);
        
        List<Location> locations = locationRepository.searchLocations(query, limit);
        
        List<AutocompleteResult> results = locations.stream()
                .map(this::convertToAutocompleteResult)
                .collect(Collectors.toList());
        
        return AutocompleteResponse.builder()
                .results(results)
                .totalCount(results.size())
                .build();
    }
    
    /**
     * Get hotel IDs for a location (used by HotelSearchService)
     */
    public List<String> getHotelIdsByLocation(String locationId, Integer minRating) {
        log.debug("Getting hotel IDs for location: {}, minRating: {}", locationId, minRating);
        return locationRepository.getHotelIdsByLocation(locationId, minRating);
    }

    public LocationHotelsResponse getHotelsByLocation(String locationId, Integer minRating) {
        log.debug("Getting hotels for location: {}, minRating: {}", locationId, minRating);
        
        Location location = locationRepository.findByLocationId(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found: " + locationId));
        
        List<String> hotelIds = locationRepository.getHotelIdsByLocation(locationId, minRating);
        
        Map<String, Object> appliedFilters = new HashMap<>();
        if (minRating != null && minRating > 0) {
            appliedFilters.put("minRating", minRating);
        }
        
        return LocationHotelsResponse.builder()
                .locationId(location.getLocationId())
                .locationName(location.getName())
                .locationType(location.getType())
                .hotelIds(hotelIds)
                .totalCount(hotelIds.size())
                .appliedFilters(appliedFilters)
                .build();
    }

    public BatchLocationResponse getBatchLocations(List<String> locationIds) {
        log.debug("Getting batch locations for {} IDs", locationIds.size());
        
        List<Location> locations = locationRepository.findByLocationIds(locationIds);
        
        List<LocationDetails> details = locations.stream()
                .map(this::convertToLocationDetails)
                .collect(Collectors.toList());
        
        return BatchLocationResponse.builder()
                .locations(details)
                .totalCount(details.size())
                .build();
    }

    public void reloadData() {
        log.info("Reloading location data");
        locationRepository.loadData();
    }

    public ServiceStats getStats() {
        return ServiceStats.builder()
                .dataSource(getDataSourceType())
                .totalLocations(locationRepository.getTotalLocations())
                .totalCities(locationRepository.getTotalCities())
                .totalStates(locationRepository.getTotalStates())
                .totalHotels(locationRepository.getTotalHotels())
                .lastLoadedAt(getLastLoadedTime())
                .status("HEALTHY")
                .build();
    }

    private AutocompleteResult convertToAutocompleteResult(Location location) {
        return AutocompleteResult.builder()
                .locationId(location.getLocationId())
                .type(location.getType())
                .displayName(buildDisplayName(location))
                .cityName(location.getCityName())
                .stateName(location.getStateName())
                .countryCode(location.getCountryCode())
                .countryName(location.getCountryName())
                .hotelCount(location.getHotelCount())
                .build();
    }

    private LocationDetails convertToLocationDetails(Location location) {
        return LocationDetails.builder()
                .locationId(location.getLocationId())
                .name(location.getName())
                .type(location.getType())
                .cityName(location.getCityName())
                .stateName(location.getStateName())
                .countryCode(location.getCountryCode())
                .countryName(location.getCountryName())
                .hotelCount(location.getHotelCount())
                .build();
    }

    private String buildDisplayName(Location location) {
        switch (location.getType()) {
            case CITY:
                return String.format("%s, %s, %s", 
                        location.getName(), 
                        location.getStateName(), 
                        location.getCountryName());
            case STATE:
                return String.format("%s, %s", 
                        location.getName(), 
                        location.getCountryName());
            default:
                return location.getName();
        }
    }

    public LocationSuggestionsResponse getLocationSuggestions(String query, int limit) {
        log.debug("Getting location suggestions for query: {}, limit: {}", query, limit);
        
        List<Location> locations = locationRepository.searchLocations(query, limit);
        
        List<LocationSuggestion> suggestions = locations.stream()
                .map(this::convertToLocationSuggestion)
                .collect(Collectors.toList());
        
        return LocationSuggestionsResponse.builder()
                .suggestions(suggestions)
                .totalCount(suggestions.size())
                .build();
    }

    public HotelSearchResponse searchHotels(HotelSearchRequest request) {
        log.info("Searching hotels for location: {}, check-in: {}, check-out: {}", 
                request.getLocationId(), request.getCheckInDate(), request.getCheckOutDate());
        
        Location location = locationRepository.findByLocationId(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found: " + request.getLocationId()));
        
        List<String> hotelIds = locationRepository.getHotelIdsByLocation(
                request.getLocationId(), 
                request.getMinRating()
        );
        
        List<HotelSearchResponse.HotelInfo> hotels = hotelIds.stream()
                .map(hotelId -> HotelSearchResponse.HotelInfo.builder()
                        .hotelId(hotelId)
                        .hotelName("Hotel " + hotelId)
                        .cityName(location.getCityName())
                        .stateName(location.getStateName())
                        .countryCode(location.getCountryCode())
                        .build())
                .collect(Collectors.toList());
        
        int nights = (int) java.time.temporal.ChronoUnit.DAYS.between(
                request.getCheckInDate(), 
                request.getCheckOutDate()
        );
        
        int totalGuests = request.getRooms().stream()
                .mapToInt(room -> room.getAdults() + (room.getChildren() != null ? room.getChildren() : 0))
                .sum();
        
        HotelSearchResponse.SearchCriteria criteria = HotelSearchResponse.SearchCriteria.builder()
                .nights(nights)
                .rooms(request.getRooms().size())
                .guests(totalGuests)
                .minRating(request.getMinRating())
                .build();
        
        return HotelSearchResponse.builder()
                .locationId(location.getLocationId())
                .locationName(location.getName())
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .hotels(hotels)
                .totalCount(hotels.size())
                .searchCriteria(criteria)
                .build();
    }

    private LocationSuggestion convertToLocationSuggestion(Location location) {
        String subtitle = buildSubtitle(location);
        String type = mapLocationType(location.getType());
        
        return LocationSuggestion.builder()
                .id(location.getLocationId())
                .name(location.getName())
                .type(type)
                .subtitle(subtitle)
                .cityName(location.getCityName())
                .stateName(location.getStateName())
                .countryName(location.getCountryName())
                .countryCode(location.getCountryCode())
                .hotelCount(location.getHotelCount())
                .build();
    }

    private String buildSubtitle(Location location) {
        switch (location.getType()) {
            case CITY:
                return String.format("city - %s, %s", 
                        location.getStateName(), 
                        location.getCountryName());
            case STATE:
                return String.format("state - %s", 
                        location.getCountryName());
            default:
                return location.getCountryName();
        }
    }

    private String mapLocationType(LocationType type) {
        switch (type) {
            case CITY:
                return "city";
            case STATE:
                return "area";
            default:
                return "country";
        }
    }

    private String getDataSourceType() {
        return locationRepository.getClass().getSimpleName().replace("LocationRepository", "");
    }

    private java.time.LocalDateTime getLastLoadedTime() {
        if (locationRepository instanceof com.example.travelhub.locationservice.repository.impl.FileBasedLocationRepository) {
            return ((com.example.travelhub.locationservice.repository.impl.FileBasedLocationRepository) locationRepository)
                    .getLastLoadedAt();
        }
        return null;
    }
}
