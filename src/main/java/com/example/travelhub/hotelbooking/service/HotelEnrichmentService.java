package com.example.travelhub.hotelbooking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.travelhub.hotelbooking.models.HotelInfo;
import com.example.travelhub.hotelbooking.models.HotelOption;
import com.example.travelhub.hotelbooking.models.HotelSearchResponse;
import com.example.travelhub.locationservice.model.HotelDetails;
import com.example.travelhub.locationservice.repository.HotelDataRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HotelEnrichmentService {
    
    private final HotelDataRepository hotelDataRepository;
    
    public HotelEnrichmentService(HotelDataRepository hotelDataRepository) {
        this.hotelDataRepository = hotelDataRepository;
    }
    
    /**
     * Enrich TripJack response with static hotel data (images, amenities, etc.)
     */
    public HotelSearchResponse enrichWithStaticData(HotelSearchResponse tripJackResponse) {
        if (tripJackResponse == null ||
            tripJackResponse.getSearchResult() == null ||
            tripJackResponse.getSearchResult().getHis() == null) {
            log.warn("TripJack response is null or empty, skipping enrichment");
            return tripJackResponse;
        }

        // Get hotel IDs from TripJack response
        List<String> hotelIds = tripJackResponse.getSearchResult().getHis().stream()
                .map(HotelInfo::getId)
                .collect(Collectors.toList());

        log.info("Enriching {} hotels with static data", hotelIds.size());

        // Fetch static data for all hotels
        Map<String, HotelDetails> detailsMap = hotelDataRepository.findByHotelIdsAsMap(hotelIds);

        log.info("Found static data for {} out of {} hotels", detailsMap.size(), hotelIds.size());

        // Merge TripJack data with static data
        tripJackResponse.getSearchResult().getHis().forEach(hotel -> {
            HotelDetails details = detailsMap.get(hotel.getId());

            if (details != null) {
                // Enrich with static data from database
                enrichHotelInfoWithDetails(hotel, details);
            } else {
                // No static data found, use defaults
                log.debug("No static data found for hotel: {}, using defaults", hotel.getId());
                enrichHotelInfoWithDefaults(hotel);
            }
        });

        return tripJackResponse;
    }

    /**
     * Enrich hotel info with data from HotelDetails entity
     */
    private void enrichHotelInfoWithDetails(HotelInfo hotel, HotelDetails details) {
        log.debug("Enriching hotel {} with database details", hotel.getId());
        
        // Images
        if (details.getImages() != null && !details.getImages().isEmpty()) {
            hotel.setImages(details.getImages());
        } else {
            hotel.setImages(getDefaultImages(hotel.getRt()));
        }
        
        // Amenities
        if (details.getAmenities() != null && !details.getAmenities().isEmpty()) {
            hotel.setAmenities(details.getAmenities());
        } else {
            hotel.setAmenities(getDefaultAmenities(hotel.getRt()));
        }
        
        // Coordinates from HotelDetails.Address.Coordinates
        if (details.getAddress() != null && 
            details.getAddress().getCoordinates() != null) {
            HotelInfo.Coordinates coordinates = HotelInfo.Coordinates.builder()
                    .lat(details.getAddress().getCoordinates().getLatitude())
                    .lng(details.getAddress().getCoordinates().getLongitude())
                    .build();
            hotel.setCoordinates(coordinates);
            
            // Calculate distance from center if coordinates are available
            // You'll need to implement this based on your city center coordinates
            hotel.setDistanceFromCenter(calculateDistanceFromCenter(coordinates));
        } else {
            hotel.setCoordinates(HotelInfo.Coordinates.builder()
                    .lat(0.0)
                    .lng(0.0)
                    .build());
            hotel.setDistanceFromCenter(0.0);
        }
        
        // Hotel chain from metadata or set as Independent
        if (details.getMetadata() != null && details.getMetadata().containsKey("chain")) {
            hotel.setChain((String) details.getMetadata().get("chain"));
        } else {
            hotel.setChain("Independent");
        }
        
        // Star rating (prefer HotelDetails value if available)
        if (details.getStarRating() != null) {
            hotel.setStars(details.getStarRating());
        } else if (hotel.getRt() != null) {
            hotel.setStars(hotel.getRt()); // Use TripJack rating as stars
        } else {
            hotel.setStars(3); // Default
        }
        
        // Guest rating from metadata
        if (details.getMetadata() != null && details.getMetadata().containsKey("guestRating")) {
            Object ratingObj = details.getMetadata().get("guestRating");
            if (ratingObj instanceof Number) {
                hotel.setGuestRating(((Number) ratingObj).doubleValue());
            }
        } else {
            // Convert TripJack rating (1-5) to guest rating (1-10)
            hotel.setGuestRating(hotel.getRt() != null ? hotel.getRt() * 2.0 : 7.0);
        }
        
        // Review count from metadata
        if (details.getMetadata() != null && details.getMetadata().containsKey("reviewCount")) {
            Object reviewCountObj = details.getMetadata().get("reviewCount");
            if (reviewCountObj instanceof Number) {
                hotel.setReviewCount(((Number) reviewCountObj).intValue());
            }
        } else {
            hotel.setReviewCount(0);
        }
        
        // Description
        if (details.getDescription() != null && !details.getDescription().isEmpty()) {
            hotel.setDescription(details.getDescription());
        }
        
        // Check-in/out times - only set defaults if TripJack didn't provide them
        if (hotel.getCheckInTime() == null) {
            if (details.getMetadata() != null && details.getMetadata().containsKey("checkInTime")) {
                hotel.setCheckInTime(HotelInfo.CheckInOutTime.builder()
                        .beginTime((String) details.getMetadata().get("checkInTime"))
                        .build());
            } else {
                hotel.setCheckInTime(HotelInfo.CheckInOutTime.builder()
                        .beginTime("14:00")
                        .build());
            }
        }
        
        if (hotel.getCheckOutTime() == null) {
            if (details.getMetadata() != null && details.getMetadata().containsKey("checkOutTime")) {
                hotel.setCheckOutTime(HotelInfo.CheckInOutTime.builder()
                        .beginTime((String) details.getMetadata().get("checkOutTime"))
                        .build());
            } else {
                hotel.setCheckOutTime(HotelInfo.CheckInOutTime.builder()
                        .beginTime("11:00")
                        .build());
            }
        }
        
        // Generate badges
        hotel.setBadges(determineBadges(hotel, details));
    }

    /**
     * Enrich hotel info with default values when no database details available
     */
    private void enrichHotelInfoWithDefaults(HotelInfo hotel) {
        log.debug("Enriching hotel {} with default values", hotel.getId());
        
        // Default images based on rating
        hotel.setImages(getDefaultImages(hotel.getRt()));
        
        // Default amenities based on rating
        hotel.setAmenities(getDefaultAmenities(hotel.getRt()));
        
        // Default coordinates (0,0) - map won't show this hotel properly
        hotel.setCoordinates(HotelInfo.Coordinates.builder()
                .lat(0.0)
                .lng(0.0)
                .build());
        
        // Default distance
        hotel.setDistanceFromCenter(0.0);
        
        // Default chain
        hotel.setChain("Independent");
        
        // Star rating from TripJack rating
        hotel.setStars(hotel.getRt() != null ? hotel.getRt() : 3);
        
        // Convert TripJack rating to guest rating
        hotel.setGuestRating(hotel.getRt() != null ? hotel.getRt() * 2.0 : 7.0);
        
        // Default review count
        hotel.setReviewCount(0);
        
        // Default check-in/out times - only if TripJack didn't provide them
        if (hotel.getCheckInTime() == null) {
            hotel.setCheckInTime(HotelInfo.CheckInOutTime.builder()
                    .beginTime("14:00")
                    .build());
        }
        if (hotel.getCheckOutTime() == null) {
            hotel.setCheckOutTime(HotelInfo.CheckInOutTime.builder()
                    .beginTime("11:00")
                    .build());
        }
        
        // Generate badges
        hotel.setBadges(determineBadges(hotel, null));
    }

    /**
     * Calculate distance from city center
     * You'll need to determine the center coordinates based on the search location
     */
    private Double calculateDistanceFromCenter(HotelInfo.Coordinates hotelCoords) {
        // TODO: Implement actual distance calculation
        // For now, return a placeholder
        // You would typically use the Haversine formula:
        // double R = 6371; // Radius of the earth in km
        // Calculate distance between hotelCoords and city center coords
        
        return 0.0; // Placeholder
    }

    /**
     * Get default images based on hotel rating
     */
    private List<String> getDefaultImages(Integer rating) {
        if (rating == null || rating < 3) {
            return List.of(
                "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=600"
            );
        } else if (rating == 3) {
            return List.of(
                "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=600",
                "https://images.unsplash.com/photo-1584132967334-10e028bd69f7?w=600"
            );
        } else if (rating == 4) {
            return List.of(
                "https://images.unsplash.com/photo-1571896349842-33c89424de2d?w=600",
                "https://images.unsplash.com/photo-1566665797739-1674de7a421a?w=600",
                "https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=600"
            );
        } else { // 5 stars
            return List.of(
                "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600",
                "https://images.unsplash.com/photo-1582719508461-905c673771fd?w=600",
                "https://images.unsplash.com/photo-1590490360182-c33d57733427?w=600"
            );
        }
    }

    /**
     * Get default amenities based on hotel rating
     */
    private List<String> getDefaultAmenities(Integer rating) {
        if (rating == null || rating < 3) {
            return List.of("Free WiFi", "Air Conditioning");
        } else if (rating == 3) {
            return List.of("Free WiFi", "Restaurant", "Parking", "Air Conditioning");
        } else if (rating == 4) {
            return List.of("Free WiFi", "Pool", "Gym", "Restaurant", "Bar", "Parking");
        } else { // 5 stars
            return List.of("Free WiFi", "Pool", "Gym", "Spa", "Restaurant", "Bar", "Parking", "Airport Shuttle");
        }
    }

    /**
     * Determine badges based on hotel characteristics
     */
    private List<String> determineBadges(HotelInfo hotel, HotelDetails details) {
        List<String> badges = new ArrayList<>();
        
        // Top Pick for high-rated hotels
        Integer rating = hotel.getStars() != null ? hotel.getStars() : hotel.getRt();
        if (rating != null && rating >= 4) {
            badges.add("Top Pick");
        }
        
        // Luxury for 5-star hotels
        if (rating != null && rating == 5) {
            badges.add("Luxury");
        }
        
        // Great Value for affordable hotels
        if (hotel.getOps() != null && !hotel.getOps().isEmpty()) {
            double lowestPrice = hotel.getOps().stream()
                    .mapToDouble(HotelOption::getTp)
                    .min()
                    .orElse(Double.MAX_VALUE);
            
            // Adjust threshold based on your market (INR in this case)
            if (lowestPrice < 2000) {
                badges.add("Great Value");
            }
            
            if (lowestPrice < 1000) {
                badges.add("Budget Friendly");
            }
        }
        
        // Free cancellation badge
        if (hotel.getIfc() != null && hotel.getIfc()) {
            badges.add("Free Cancellation");
        }
        
        // Boutique for independent hotels with high ratings
        if ("Independent".equals(hotel.getChain()) && rating != null && rating >= 4) {
            badges.add("Boutique");
        }
        
        // Check metadata for special badges
        if (details != null && details.getMetadata() != null) {
            if (details.getMetadata().containsKey("award") && 
                Boolean.TRUE.equals(details.getMetadata().get("award"))) {
                badges.add("Award Winner");
            }
            
            if (details.getMetadata().containsKey("newlyRenovated") && 
                Boolean.TRUE.equals(details.getMetadata().get("newlyRenovated"))) {
                badges.add("Newly Renovated");
            }
        }
        
        return badges;
    }
}