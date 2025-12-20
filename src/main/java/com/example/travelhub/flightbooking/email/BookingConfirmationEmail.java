package com.example.travelhub.flightbooking.email;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO containing booking confirmation details for email
 */
public class BookingConfirmationEmail {
    
    private String bookingId;
    private String pnr;
    private String bookingStatus;
    
    // Flight details
    private String origin;
    private String destination;
    private String departureDate;
    private String departureTime;
    private String arrivalDate;
    private String arrivalTime;
    private String airlineName;
    private String airlineCode;
    private String flightNumber;
    private String cabinClass;
    
    // Passenger details
    private List<PassengerDetail> passengers;
    
    // Price details
    private BigDecimal totalFare;
    private String currency;
    
    // Contact details
    private String recipientEmail;
    private String recipientName;
    
    public BookingConfirmationEmail() {}
    
    public BookingConfirmationEmail(String bookingId, String pnr, String bookingStatus, String origin,
            String destination, String departureDate, String departureTime, String arrivalDate,
            String arrivalTime, String airlineName, String airlineCode, String flightNumber,
            String cabinClass, List<PassengerDetail> passengers, BigDecimal totalFare, String currency,
            String recipientEmail, String recipientName) {
        this.bookingId = bookingId;
        this.pnr = pnr;
        this.bookingStatus = bookingStatus;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        this.airlineName = airlineName;
        this.airlineCode = airlineCode;
        this.flightNumber = flightNumber;
        this.cabinClass = cabinClass;
        this.passengers = passengers;
        this.totalFare = totalFare;
        this.currency = currency;
        this.recipientEmail = recipientEmail;
        this.recipientName = recipientName;
    }
    
    public static BookingConfirmationEmailBuilder builder() {
        return new BookingConfirmationEmailBuilder();
    }
    
    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    
    public String getPnr() { return pnr; }
    public void setPnr(String pnr) { this.pnr = pnr; }
    
    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    
    public String getDepartureDate() { return departureDate; }
    public void setDepartureDate(String departureDate) { this.departureDate = departureDate; }
    
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    
    public String getArrivalDate() { return arrivalDate; }
    public void setArrivalDate(String arrivalDate) { this.arrivalDate = arrivalDate; }
    
    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
    
    public String getAirlineName() { return airlineName; }
    public void setAirlineName(String airlineName) { this.airlineName = airlineName; }
    
    public String getAirlineCode() { return airlineCode; }
    public void setAirlineCode(String airlineCode) { this.airlineCode = airlineCode; }
    
    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    
    public String getCabinClass() { return cabinClass; }
    public void setCabinClass(String cabinClass) { this.cabinClass = cabinClass; }
    
    public List<PassengerDetail> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerDetail> passengers) { this.passengers = passengers; }
    
    public BigDecimal getTotalFare() { return totalFare; }
    public void setTotalFare(BigDecimal totalFare) { this.totalFare = totalFare; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }
    
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    
    // Builder class
    public static class BookingConfirmationEmailBuilder {
        private String bookingId;
        private String pnr;
        private String bookingStatus;
        private String origin;
        private String destination;
        private String departureDate;
        private String departureTime;
        private String arrivalDate;
        private String arrivalTime;
        private String airlineName;
        private String airlineCode;
        private String flightNumber;
        private String cabinClass;
        private List<PassengerDetail> passengers;
        private BigDecimal totalFare;
        private String currency;
        private String recipientEmail;
        private String recipientName;
        
        public BookingConfirmationEmailBuilder bookingId(String bookingId) { this.bookingId = bookingId; return this; }
        public BookingConfirmationEmailBuilder pnr(String pnr) { this.pnr = pnr; return this; }
        public BookingConfirmationEmailBuilder bookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; return this; }
        public BookingConfirmationEmailBuilder origin(String origin) { this.origin = origin; return this; }
        public BookingConfirmationEmailBuilder destination(String destination) { this.destination = destination; return this; }
        public BookingConfirmationEmailBuilder departureDate(String departureDate) { this.departureDate = departureDate; return this; }
        public BookingConfirmationEmailBuilder departureTime(String departureTime) { this.departureTime = departureTime; return this; }
        public BookingConfirmationEmailBuilder arrivalDate(String arrivalDate) { this.arrivalDate = arrivalDate; return this; }
        public BookingConfirmationEmailBuilder arrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; return this; }
        public BookingConfirmationEmailBuilder airlineName(String airlineName) { this.airlineName = airlineName; return this; }
        public BookingConfirmationEmailBuilder airlineCode(String airlineCode) { this.airlineCode = airlineCode; return this; }
        public BookingConfirmationEmailBuilder flightNumber(String flightNumber) { this.flightNumber = flightNumber; return this; }
        public BookingConfirmationEmailBuilder cabinClass(String cabinClass) { this.cabinClass = cabinClass; return this; }
        public BookingConfirmationEmailBuilder passengers(List<PassengerDetail> passengers) { this.passengers = passengers; return this; }
        public BookingConfirmationEmailBuilder totalFare(BigDecimal totalFare) { this.totalFare = totalFare; return this; }
        public BookingConfirmationEmailBuilder currency(String currency) { this.currency = currency; return this; }
        public BookingConfirmationEmailBuilder recipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; return this; }
        public BookingConfirmationEmailBuilder recipientName(String recipientName) { this.recipientName = recipientName; return this; }
        
        public BookingConfirmationEmail build() {
            return new BookingConfirmationEmail(bookingId, pnr, bookingStatus, origin, destination,
                    departureDate, departureTime, arrivalDate, arrivalTime, airlineName, airlineCode,
                    flightNumber, cabinClass, passengers, totalFare, currency, recipientEmail, recipientName);
        }
    }
    
    // PassengerDetail inner class
    public static class PassengerDetail {
        private String title;
        private String firstName;
        private String lastName;
        private String paxType;
        private String ticketNumber;
        
        public PassengerDetail() {}
        
        public PassengerDetail(String title, String firstName, String lastName, String paxType, String ticketNumber) {
            this.title = title;
            this.firstName = firstName;
            this.lastName = lastName;
            this.paxType = paxType;
            this.ticketNumber = ticketNumber;
        }
        
        public static PassengerDetailBuilder builder() {
            return new PassengerDetailBuilder();
        }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        
        public String getPaxType() { return paxType; }
        public void setPaxType(String paxType) { this.paxType = paxType; }
        
        public String getTicketNumber() { return ticketNumber; }
        public void setTicketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; }
        
        public static class PassengerDetailBuilder {
            private String title;
            private String firstName;
            private String lastName;
            private String paxType;
            private String ticketNumber;
            
            public PassengerDetailBuilder title(String title) { this.title = title; return this; }
            public PassengerDetailBuilder firstName(String firstName) { this.firstName = firstName; return this; }
            public PassengerDetailBuilder lastName(String lastName) { this.lastName = lastName; return this; }
            public PassengerDetailBuilder paxType(String paxType) { this.paxType = paxType; return this; }
            public PassengerDetailBuilder ticketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; return this; }
            
            public PassengerDetail build() {
                return new PassengerDetail(title, firstName, lastName, paxType, ticketNumber);
            }
        }
    }
}
