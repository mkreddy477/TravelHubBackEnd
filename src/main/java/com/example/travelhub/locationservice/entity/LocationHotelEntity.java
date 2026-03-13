package com.example.travelhub.locationservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "location_hotels", indexes = {
    @Index(name = "idx_location_hotel", columnList = "location_id, hotel_id"),
    @Index(name = "idx_hotel_rating", columnList = "hotel_id, rating")
})
public class LocationHotelEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "location_id", nullable = false, length = 100)
    private String locationId;
    
    @Column(name = "hotel_id", nullable = false, length = 50)
    private String hotelId;
    
    @Column(name = "rating")
    private Integer rating;
    
    // Constructors
    public LocationHotelEntity() {
    }
    
    public LocationHotelEntity(Long id, String locationId, String hotelId, Integer rating) {
        this.id = id;
        this.locationId = locationId;
        this.hotelId = hotelId;
        this.rating = rating;
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public String getLocationId() {
        return locationId;
    }
    
    public String getHotelId() {
        return hotelId;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    
    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    // Builder pattern (manual implementation)
    public static LocationHotelEntityBuilder builder() {
        return new LocationHotelEntityBuilder();
    }
    
    public static class LocationHotelEntityBuilder {
        private Long id;
        private String locationId;
        private String hotelId;
        private Integer rating;
        
        public LocationHotelEntityBuilder id(Long id) {
            this.id = id;
            return this;
        }
        
        public LocationHotelEntityBuilder locationId(String locationId) {
            this.locationId = locationId;
            return this;
        }
        
        public LocationHotelEntityBuilder hotelId(String hotelId) {
            this.hotelId = hotelId;
            return this;
        }
        
        public LocationHotelEntityBuilder rating(Integer rating) {
            this.rating = rating;
            return this;
        }
        
        public LocationHotelEntity build() {
            return new LocationHotelEntity(id, locationId, hotelId, rating);
        }
    }
    
    // equals, hashCode, toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationHotelEntity that = (LocationHotelEntity) o;
        return id != null && id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "LocationHotelEntity{" +
                "id=" + id +
                ", locationId='" + locationId + '\'' +
                ", hotelId='" + hotelId + '\'' +
                ", rating=" + rating +
                '}';
    }
}