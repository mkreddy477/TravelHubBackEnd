package com.example.travelhub.flightbooking.dao;

import java.math.BigDecimal;

import com.example.travelhub.flightbooking.models.bookingmodels.BookingRequest;

public interface IFlightBookingDao {

    void saveBooking(BookingRequest bookingRequest, BigDecimal totalAmount);
}
