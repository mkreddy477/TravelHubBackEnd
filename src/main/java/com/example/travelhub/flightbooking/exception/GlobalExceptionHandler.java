package com.example.travelhub.flightbooking.exception;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.example.travelhub.flightbooking.models.reviewmodels.ErrorResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Global exception handler for reactive REST controllers
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(FlightServiceException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleFlightServiceException(
            FlightServiceException ex) {
        
        log.error("FlightServiceException occurred: {}", ex.getMessage());
		return null;
  

}
    }

