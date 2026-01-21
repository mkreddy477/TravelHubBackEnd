package com.example.travelhub.locationservice.repository.impl;

import com.example.travelhub.locationservice.entity.LocationEntity;
import com.example.travelhub.locationservice.entity.LocationHotelEntity;
import com.example.travelhub.locationservice.model.HotelData;
import com.example.travelhub.locationservice.model.Location;
import com.example.travelhub.locationservice.model.LocationType;
import com.example.travelhub.locationservice.repository.LocationRepository;
import com.example.travelhub.locationservice.repository.jpa.LocationHotelJpaRepository;
import com.example.travelhub.locationservice.repository.jpa.LocationJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@ConditionalOnProperty(name = "location.datasource.type", havingValue = "database")
public class DatabaseLocationRepository implements LocationRepository {

    @Value("${location.datasource.file.path:}")
    private String filePath;

    private final LocationJpaRepository locationJpaRepository;
    private final LocationHotelJpaRepository locationHotelJpaRepository;
    private final ObjectMapper objectMapper;

    public DatabaseLocationRepository(
            LocationJpaRepository locationJpaRepository,
            LocationHotelJpaRepository locationHotelJpaRepository,
            ObjectMapper objectMapper) {
        this.locationJpaRepository = locationJpaRepository;
        this.locationHotelJpaRepository = locationHotelJpaRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Cacheable(value = "locationSearch", key = "#query + '_' + #limit")
    public List<Location> searchLocations(String query, int limit) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<LocationEntity> entities = locationJpaRepository.searchByName(query);
        return entities.stream()
                .limit(limit)
                .map(this::convertToLocation)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "locationById", key = "#locationId")
    public Optional<Location> findByLocationId(String locationId) {
        return locationJpaRepository.findById(locationId)
                .map(this::convertToLocation);
    }

    @Override
    public List<Location> findByLocationIds(List<String> locationIds) {
        return locationJpaRepository.findByLocationIdIn(locationIds).stream()
                .map(this::convertToLocation)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "locationHotels", key = "#locationId + '_' + #minRating")
    public List<String> getHotelIdsByLocation(String locationId, Integer minRating) {
        if (minRating == null || minRating <= 0) {
            return locationHotelJpaRepository.findByLocationId(locationId).stream()
                    .map(LocationHotelEntity::getHotelId)
                    .collect(Collectors.toList());
        }

        return locationHotelJpaRepository.findHotelIdsByLocationAndMinRating(locationId, minRating);
    }

    @Override
    @Transactional
    public void loadData() {
        if (filePath == null || filePath.isEmpty()) {
        	System.out.println("No file path configured for database loading");
            return;
        }

        System.out.println("Loading hotel data from file into database: "+ filePath);
        try {
            File file = new File(filePath);
            if (!file.exists()) {
            	System.out.println("Hotel data file not found: "+ filePath);
                throw new RuntimeException("Hotel data file not found: " + filePath);
            }

            HotelData hotelData = objectMapper.readValue(file, HotelData.class);
            
            locationJpaRepository.deleteAll();
            locationHotelJpaRepository.deleteAll();
            
            Map<String, LocationData> cityData = new HashMap<>();
            Map<String, LocationData> stateData = new HashMap<>();

            for (HotelData.HotelInfo hotel : hotelData.getHotelOpInfos()) {
                if (hotel.getAddress() == null) continue;

                String cityName = hotel.getAddress().getCity() != null ? 
                        hotel.getAddress().getCity().getName() : null;
                String stateName = hotel.getAddress().getState() != null ? 
                        hotel.getAddress().getState().getName() : null;
                String countryCode = hotel.getAddress().getCountry() != null ? 
                        hotel.getAddress().getCountry().getCode() : null;
                String countryName = hotel.getAddress().getCountry() != null ? 
                        hotel.getAddress().getCountry().getName() : null;

                if (cityName != null && stateName != null && countryCode != null) {
                    String cityKey = generateCityKey(cityName, stateName, countryCode);
                    LocationData cityLoc = cityData.computeIfAbsent(cityKey, 
                            k -> new LocationData(cityName, stateName, countryCode, countryName));
                    cityLoc.addHotel(hotel.getHotelId(), hotel.getRating());

                    String stateKey = generateStateKey(stateName, countryCode);
                    LocationData stateLoc = stateData.computeIfAbsent(stateKey, 
                            k -> new LocationData(stateName, stateName, countryCode, countryName));
                    stateLoc.addHotel(hotel.getHotelId(), hotel.getRating());
                }
            }

            List<LocationEntity> locationEntities = new ArrayList<>();
            List<LocationHotelEntity> hotelEntities = new ArrayList<>();

            for (Map.Entry<String, LocationData> entry : cityData.entrySet()) {
                LocationData data = entry.getValue();
                locationEntities.add(LocationEntity.builder()
                        .locationId(entry.getKey())
                        .name(data.name)
                        .type(LocationType.CITY)
                        .cityName(data.name)
                        .stateName(data.stateName)
                        .countryCode(data.countryCode)
                        .countryName(data.countryName)
                        .hotelCount(data.hotels.size())
                        .build());

                for (Map.Entry<String, Integer> hotel : data.hotels.entrySet()) {
                    hotelEntities.add(LocationHotelEntity.builder()
                            .locationId(entry.getKey())
                            .hotelId(hotel.getKey())
                            .rating(hotel.getValue())
                            .build());
                }
            }

            for (Map.Entry<String, LocationData> entry : stateData.entrySet()) {
                LocationData data = entry.getValue();
                locationEntities.add(LocationEntity.builder()
                        .locationId(entry.getKey())
                        .name(data.stateName)
                        .type(LocationType.STATE)
                        .stateName(data.stateName)
                        .countryCode(data.countryCode)
                        .countryName(data.countryName)
                        .hotelCount(data.hotels.size())
                        .build());

                for (Map.Entry<String, Integer> hotel : data.hotels.entrySet()) {
                    hotelEntities.add(LocationHotelEntity.builder()
                            .locationId(entry.getKey())
                            .hotelId(hotel.getKey())
                            .rating(hotel.getValue())
                            .build());
                }
            }

            locationJpaRepository.saveAll(locationEntities);
            locationHotelJpaRepository.saveAll(hotelEntities);

            System.out.println("Successfully loaded " + locationEntities.size() + 
                    " locations with " + hotelEntities.size() + " hotel mappings into database");
        } catch (IOException e) {
            System.out.println("Error loading hotel data from file" + e);
            throw new RuntimeException("Failed to load hotel data", e);
        }
    }

    @Override
    public int getTotalLocations() {
        return (int) locationJpaRepository.count();
    }

    @Override
    public int getTotalCities() {
        return locationJpaRepository.findAll().stream()
                .filter(loc -> loc.getType() == LocationType.CITY)
                .mapToInt(e -> 1)
                .sum();
    }

    @Override
    public int getTotalStates() {
        return locationJpaRepository.findAll().stream()
                .filter(loc -> loc.getType() == LocationType.STATE)
                .mapToInt(e -> 1)
                .sum();
    }

    @Override
    public int getTotalHotels() {
        return locationJpaRepository.findAll().stream()
                .filter(loc -> loc.getType() == LocationType.CITY)
                .mapToInt(LocationEntity::getHotelCount)
                .sum();
    }

    private Location convertToLocation(LocationEntity entity) {
        return Location.builder()
                .locationId(entity.getLocationId())
                .name(entity.getName())
                .type(entity.getType())
                .cityName(entity.getCityName())
                .stateName(entity.getStateName())
                .countryCode(entity.getCountryCode())
                .countryName(entity.getCountryName())
                .hotelCount(entity.getHotelCount())
                .build();
    }

    private String generateCityKey(String city, String state, String countryCode) {
        return String.format("city_%s_%s_%s", 
                normalizeForKey(city), 
                normalizeForKey(state), 
                countryCode.toLowerCase());
    }

    private String generateStateKey(String state, String countryCode) {
        return String.format("state_%s_%s", 
                normalizeForKey(state), 
                countryCode.toLowerCase());
    }

    private String normalizeForKey(String value) {
        return value.toLowerCase()
                .replaceAll("[^a-z0-9]", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");
    }

    private static class LocationData {
        String name;
        String stateName;
        String countryCode;
        String countryName;
        Map<String, Integer> hotels = new HashMap<>();

        LocationData(String name, String stateName, String countryCode, String countryName) {
            this.name = name;
            this.stateName = stateName;
            this.countryCode = countryCode;
            this.countryName = countryName;
        }

        void addHotel(String hotelId, Integer rating) {
            hotels.put(hotelId, rating);
        }
    }
}
