package com.example.travelhub.hotelbooking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelhub.hotelbooking.models.HotelReviewRequest;
import com.example.travelhub.hotelbooking.models.HotelReviewResponse;
import com.example.travelhub.hotelbooking.service.HotelReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelReviewController {

    private final HotelReviewService hotelReviewService;

    @PostMapping("/review")
    public Mono<ResponseEntity<HotelReviewResponse>> reviewHotel(
            @Valid @RequestBody HotelReviewRequest request) {
        
        log.info("Received hotel review request: {}", request);
        
        return hotelReviewService.reviewHotel(request)
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> log.info("Hotel review completed successfully"))
                .doOnError(error -> log.error("Error in hotel review", error));
    }
}