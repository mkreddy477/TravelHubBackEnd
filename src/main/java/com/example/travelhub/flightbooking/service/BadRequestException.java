package com.example.travelhub.flightbooking.service;

//Option 1: Custom exception
public class BadRequestException extends RuntimeException {
 public BadRequestException(String message) {
     super(message);
 }
}

