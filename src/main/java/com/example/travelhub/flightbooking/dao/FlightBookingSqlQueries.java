package com.example.travelhub.flightbooking.dao;

public final class FlightBookingSqlQueries {

    private FlightBookingSqlQueries() {
    }

    public static final String INSERT_PASSENGER = """
        INSERT INTO FLIGHT_BOOKING_PASSENGERS
        (BOOKING_DETAIL_ID, FIRST_NAME, LAST_NAME, GENDER, PASSENGER_CAT, COUNTRY_CODE, MOBILE_NO, EMAIL, IS_WHEELCHAIR_REQUIRED, IS_ACTIVE, CREATED_BY, CREATED_ON)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1, ?, ?)
        """;

    public static final String INSERT_PAYMENT_DETAILS = """
        INSERT INTO PAYMENT_DETAILS
        (BOOKING_DETAIL_ID, PAYMENT_METHOD_ID, PAYMENT_STATUS_ID, TOTAL_AMOUNT, BASE_AMOUNT, TAX_AMOUNT, INITIATED_ON, CREATED_ON, CREATED_BY, UPDATED_ON)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
}
