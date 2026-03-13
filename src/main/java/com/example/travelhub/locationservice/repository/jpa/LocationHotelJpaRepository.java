package com.example.travelhub.locationservice.repository.jpa;

import com.example.travelhub.locationservice.entity.LocationHotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationHotelJpaRepository extends JpaRepository<LocationHotelEntity, Long> {
    
    List<LocationHotelEntity> findByLocationId(String locationId);
    
    @Query("SELECT lh.hotelId FROM LocationHotelEntity lh WHERE lh.locationId = :locationId AND lh.rating >= :minRating")
    List<String> findHotelIdsByLocationAndMinRating(@Param("locationId") String locationId, @Param("minRating") Integer minRating);
}
