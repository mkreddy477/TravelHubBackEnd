package com.example.travelhub.flightbooking.service;

public class BadRequestException extends RuntimeException {
 public BadRequestException(String message) {
     super(message);
 }
}

