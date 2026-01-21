package com.example.travelhub.locationservice.repository;

import com.example.travelhub.locationservice.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationRepository {
    
    List<Location> searchLocations(String query, int limit);
    
    Optional<Location> findByLocationId(String locationId);
    
    List<Location> findByLocationIds(List<String> locationIds);
    
    List<String> getHotelIdsByLocation(String locationId, Integer minRating);
    
    void loadData();
    
    int getTotalLocations();
    
    int getTotalCities();
    
    int getTotalStates();
    
    int getTotalHotels();
}
