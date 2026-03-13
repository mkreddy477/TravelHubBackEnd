package com.example.travelhub.hotelbooking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelhub.hotelbooking.models.HotelBookingRequest;
import com.example.travelhub.hotelbooking.models.HotelBookingResponse;
import com.example.travelhub.hotelbooking.service.HotelBookingService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/hotels")
public class HotelBookingController {

    private final HotelBookingService bookingService;

    public HotelBookingController(HotelBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/booking")
    public Mono<ResponseEntity<HotelBookingResponse>> createBooking(
            @Valid @RequestBody HotelBookingRequest request) {
        
        log.info("Received hotel booking request for booking ID: {}", request.getBookingId());
        
        return bookingService.createBooking(request)
                .map(response -> ResponseEntity.ok(response))
                .onErrorResume(error -> {
                    log.error("Error processing booking request: {}", error.getMessage(), error);
                    return Mono.just(ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .build());
                });
    }
}