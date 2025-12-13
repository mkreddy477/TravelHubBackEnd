package com.example.travelhub.flightbooking.exception;

import lombok.Getter;

/**
 * Custom exception for Flight Service operations
 */
@Getter
public class FlightServiceException extends RuntimeException {
    
    private final int statusCode;
    
    public FlightServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public FlightServiceException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
