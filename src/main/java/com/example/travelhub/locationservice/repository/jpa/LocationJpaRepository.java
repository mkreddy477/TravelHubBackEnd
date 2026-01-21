package com.example.travelhub.locationservice.repository.jpa;

import com.example.travelhub.locationservice.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationJpaRepository extends JpaRepository<LocationEntity, String> {
    
    @Query("SELECT l FROM LocationEntity l WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :query, '%')) ORDER BY " +
           "CASE WHEN LOWER(l.name) LIKE LOWER(CONCAT(:query, '%')) THEN 0 ELSE 1 END, " +
           "CASE WHEN l.type = 'CITY' THEN 0 ELSE 1 END, " +
           "l.hotelCount DESC")
    List<LocationEntity> searchByName(@Param("query") String query);
    
    List<LocationEntity> findByLocationIdIn(List<String> locationIds);
}
