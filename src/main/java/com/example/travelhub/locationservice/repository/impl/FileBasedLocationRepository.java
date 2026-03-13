package com.example.travelhub.locationservice.repository.impl;

import com.example.travelhub.locationservice.model.HotelData;
import com.example.travelhub.locationservice.model.Location;
import com.example.travelhub.locationservice.model.LocationType;
import com.example.travelhub.locationservice.repository.LocationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import java.io.InputStream;

@Slf4j
@Repository
@ConditionalOnProperty(name = "location.datasource.type", havingValue = "file", matchIfMissing = true)
public class FileBasedLocationRepository implements LocationRepository {

    @Value("${location.datasource.file.path}")
    private String filePath;

    private final ObjectMapper objectMapper;
    private final Map<String, Location> locationIndex = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> cityIndex = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> stateIndex = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Integer>> hotelRatings = new ConcurrentHashMap<>();
    private LocalDateTime lastLoadedAt;
    private static final Logger log = LoggerFactory.getLogger(FileBasedLocationRepository.class);

    public FileBasedLocationRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        loadData();
    }

    @Override
    public void loadData() {
        log.info("Loading hotel data from file: {}", filePath);
        try {
            InputStream inputStream;
            
            // Check if it's a classpath resource
            if (filePath.startsWith("classpath:")) {
                String resourcePath = filePath.substring("classpath:".length());
                Resource resource = new ClassPathResource(resourcePath);
                if (!resource.exists()) {
                    log.error("Hotel data file not found: {}", filePath);
                    throw new RuntimeException("Hotel data file not found: " + filePath);
                }
                inputStream = resource.getInputStream();
            } else {
                // Regular file path
                File file = new File(filePath);
                if (!file.exists()) {
                    log.error("Hotel data file not found: {}", filePath);
                    throw new RuntimeException("Hotel data file not found: " + filePath);
                }
                inputStream = new FileSystemResource(file).getInputStream();
            }

            HotelData hotelData = objectMapper.readValue(inputStream, HotelData.class);
            buildIndexes(hotelData);
            lastLoadedAt = LocalDateTime.now();
            
            log.info("Successfully loaded {} locations with {} hotels", 
                    locationIndex.size(), getTotalHotels());
                    
            inputStream.close(); // Close the stream
            
        } catch (IOException e) {
            log.error("Error loading hotel data from file", e);
            throw new RuntimeException("Failed to load hotel data", e);
        }
    }

    private void buildIndexes(HotelData hotelData) {
        locationIndex.clear();
        cityIndex.clear();
        stateIndex.clear();
        hotelRatings.clear();

        Map<String, Set<String>> cityHotels = new HashMap<>();
        Map<String, Set<String>> stateHotels = new HashMap<>();
        Map<String, LocationMetadata> cityMetadata = new HashMap<>();
        Map<String, LocationMetadata> stateMetadata = new HashMap<>();

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
                cityHotels.computeIfAbsent(cityKey, k -> new HashSet<>()).add(hotel.getHotelId());
                cityMetadata.put(cityKey, new LocationMetadata(cityName, stateName, countryCode, countryName));

                String stateKey = generateStateKey(stateName, countryCode);
                stateHotels.computeIfAbsent(stateKey, k -> new HashSet<>()).add(hotel.getHotelId());
                stateMetadata.put(stateKey, new LocationMetadata(null, stateName, countryCode, countryName));

                if (hotel.getRating() != null) {
                    hotelRatings.computeIfAbsent(cityKey, k -> new HashMap<>())
                            .put(hotel.getHotelId(), hotel.getRating());
                    hotelRatings.computeIfAbsent(stateKey, k -> new HashMap<>())
                            .put(hotel.getHotelId(), hotel.getRating());
                }
            }
        }

        for (Map.Entry<String, Set<String>> entry : cityHotels.entrySet()) {
            LocationMetadata metadata = cityMetadata.get(entry.getKey());
            Location location = Location.builder()
                    .locationId(entry.getKey())
                    .name(metadata.cityName)
                    .type(LocationType.CITY)
                    .cityName(metadata.cityName)
                    .stateName(metadata.stateName)
                    .countryCode(metadata.countryCode)
                    .countryName(metadata.countryName)
                    .hotelIds(entry.getValue())
                    .hotelCount(entry.getValue().size())
                    .build();
            locationIndex.put(entry.getKey(), location);
            
            String normalizedCity = metadata.cityName.toLowerCase();
            cityIndex.computeIfAbsent(normalizedCity, k -> new HashSet<>()).add(entry.getKey());
        }

        for (Map.Entry<String, Set<String>> entry : stateHotels.entrySet()) {
            LocationMetadata metadata = stateMetadata.get(entry.getKey());
            Location location = Location.builder()
                    .locationId(entry.getKey())
                    .name(metadata.stateName)
                    .type(LocationType.STATE)
                    .stateName(metadata.stateName)
                    .countryCode(metadata.countryCode)
                    .countryName(metadata.countryName)
                    .hotelIds(entry.getValue())
                    .hotelCount(entry.getValue().size())
                    .build();
            locationIndex.put(entry.getKey(), location);
            
            String normalizedState = metadata.stateName.toLowerCase();
            stateIndex.computeIfAbsent(normalizedState, k -> new HashSet<>()).add(entry.getKey());
        }
    }

    @Override
    public List<Location> searchLocations(String query, int limit) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String normalizedQuery = query.toLowerCase().trim();
        List<Location> results = new ArrayList<>();

        for (Location location : locationIndex.values()) {
            if (location.getName().toLowerCase().contains(normalizedQuery)) {
                results.add(location);
            }
        }

        results.sort((l1, l2) -> {
            String name1 = l1.getName().toLowerCase();
            String name2 = l2.getName().toLowerCase();
            
            boolean starts1 = name1.startsWith(normalizedQuery);
            boolean starts2 = name2.startsWith(normalizedQuery);
            
            if (starts1 && !starts2) return -1;
            if (!starts1 && starts2) return 1;
            
            if (l1.getType() == LocationType.CITY && l2.getType() == LocationType.STATE) return -1;
            if (l1.getType() == LocationType.STATE && l2.getType() == LocationType.CITY) return 1;
            
            return Integer.compare(l2.getHotelCount(), l1.getHotelCount());
        });

        return results.stream().limit(limit).collect(Collectors.toList());
    }

    @Override
    public Optional<Location> findByLocationId(String locationId) {
        return Optional.ofNullable(locationIndex.get(locationId));
    }

    @Override
    public List<Location> findByLocationIds(List<String> locationIds) {
        return locationIds.stream()
                .map(locationIndex::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getHotelIdsByLocation(String locationId, Integer minRating) {
        Location location = locationIndex.get(locationId);
        if (location == null || location.getHotelIds() == null) {
            return Collections.emptyList();
        }

        if (minRating == null || minRating <= 0) {
            return new ArrayList<>(location.getHotelIds());
        }

        Map<String, Integer> ratings = hotelRatings.get(locationId);
        if (ratings == null) {
            return new ArrayList<>(location.getHotelIds());
        }

        return location.getHotelIds().stream()
                .filter(hotelId -> {
                    Integer rating = ratings.get(hotelId);
                    return rating != null && rating >= minRating;
                })
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalLocations() {
        return locationIndex.size();
    }

    @Override
    public int getTotalCities() {
        return (int) locationIndex.values().stream()
                .filter(loc -> loc.getType() == LocationType.CITY)
                .count();
    }

    @Override
    public int getTotalStates() {
        return (int) locationIndex.values().stream()
                .filter(loc -> loc.getType() == LocationType.STATE)
                .count();
    }

    @Override
    public int getTotalHotels() {
        return locationIndex.values().stream()
                .filter(loc -> loc.getType() == LocationType.CITY)
                .mapToInt(Location::getHotelCount)
                .sum();
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

    private static class LocationMetadata {
        String cityName;
        String stateName;
        String countryCode;
        String countryName;

        LocationMetadata(String cityName, String stateName, String countryCode, String countryName) {
            this.cityName = cityName;
            this.stateName = stateName;
            this.countryCode = countryCode;
            this.countryName = countryName;
        }
    }

    public LocalDateTime getLastLoadedAt() {
        return lastLoadedAt;
    }
}
