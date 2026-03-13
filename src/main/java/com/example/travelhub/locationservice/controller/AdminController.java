package com.example.travelhub.locationservice.controller;

import com.example.travelhub.locationservice.dto.ServiceStats;
import com.example.travelhub.locationservice.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/hotels/admin")
public class AdminController {

    private final LocationService locationService;
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    public AdminController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "location-service");
        health.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.ok(health);
    }

    @PostMapping("/reload-data")
    public ResponseEntity<Map<String, Object>> reloadData() {
        log.info("Admin reload data request received");
        
        try {
            locationService.reloadData();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Data reloaded successfully");
            response.put("timestamp", java.time.LocalDateTime.now());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error reloading data", e);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ERROR");
            response.put("message", "Failed to reload data: " + e.getMessage());
            response.put("timestamp", java.time.LocalDateTime.now());
            
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<ServiceStats> getStats() {
        log.info("Admin stats request received");
        
        ServiceStats stats = locationService.getStats();
        return ResponseEntity.ok(stats);
    }
}
