package com.example.travelhub.hotelbooking.service;

import com.example.travelhub.hotelbooking.models.HotelSearchResponse;
import com.example.travelhub.locationservice.model.HotelDetails;
import com.example.travelhub.locationservice.repository.HotelDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                .map(hotel -> hotel.getId())  // Adjust based on your actual field name
                .collect(Collectors.toList());
        
        log.info("Enriching {} hotels with static data", hotelIds.size());
        
        // Fetch static data for all hotels
        Map<String, HotelDetails> detailsMap = hotelDataRepository.findByHotelIdsAsMap(hotelIds);
        
        log.info("Found static data for {} out of {} hotels", detailsMap.size(), hotelIds.size());
        
        // Merge TripJack data with static data
        tripJackResponse.getSearchResult().getHis().forEach(hotel -> {
            HotelDetails details = detailsMap.get(hotel.getId());
            
            if (details != null) {
                // Enrich with static data
                enrichHotelInfo(hotel, details);
            } else {
                log.debug("No static data found for hotel: {}", hotel.getId());
            }
        });
        
        return tripJackResponse;
    }
    
    /**
     * Enrich a single hotel with static data
     */
    private void enrichHotelInfo(HotelSearchResponse.SearchResult.HotelInfo hotel, HotelDetails details) {
        // Set images if not already present or override TripJack images
        if (details.getImages() != null && !details.getImages().isEmpty()) {
            hotel.setImages(details.getImages());  // Adjust based on your model
        }
        
        // Set amenities
        if (details.getAmenities() != null && !details.getAmenities().isEmpty()) {
            hotel.setAmenities(details.getAmenities());  // Adjust based on your model
        }
        
        // Set description
        if (details.getDescription() != null) {
            hotel.setDescription(details.getDescription());  // Adjust based on your model
        }
        
        // Set star rating (if TripJack doesn't provide it)
        if (details.getStarRating() != null) {
            hotel.setStarRating(details.getStarRating());  // Adjust based on your model
        }
        
        // You can add more fields as needed
    }
    
    /**
     * Get hotel details for a single hotel
     */
    public HotelDetails getHotelDetails(String hotelId) {
        return hotelDataRepository.findByHotelId(hotelId).orElse(null);
    }
}