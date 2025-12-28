package com.example.travelhub.flightbooking.email;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.example.travelhub.flightbooking.models.bookingmodels.BookingDetailsResponse;
import com.example.travelhub.flightbooking.models.bookingmodels.BookingRequest;
import com.example.travelhub.flightbooking.models.bookingmodels.BookingResponse;
import com.example.travelhub.flightbooking.models.bookingmodels.TravellerInfo;

/**
 * Builder to construct BookingConfirmationEmail from booking request and response
 */
@Component
public class BookingEmailBuilder {

    private static final Logger log = LoggerFactory.getLogger(BookingEmailBuilder.class);

    /**
     * Build a booking confirmation email from the booking request and response
     */
    public BookingConfirmationEmail buildFromBookingResponse(
            BookingRequest request, 
            BookingResponse response) {
        
        BookingConfirmationEmail.BookingConfirmationEmailBuilder builder = BookingConfirmationEmail.builder();
        
        // Set booking identifiers
        builder.bookingId(response.getBookingId() != null ? response.getBookingId() : request.getBookingId());
        
        // Extract flight details from itemInfos (JsonNode)
        boolean flightDetailsExtracted = false;
        try {
            flightDetailsExtracted = extractFlightDetails(response.getItemInfos(), builder);
        } catch (Exception e) {
            log.warn("Could not extract flight details from itemInfos: {}", e.getMessage());
        }
        
        // If flight details not found in itemInfos, try searchQuery for route info
        if (!flightDetailsExtracted) {
            try {
                extractRouteFromSearchQuery(response.getSearchQuery(), builder);
            } catch (Exception e) {
                log.warn("Could not extract route from searchQuery: {}", e.getMessage());
            }
        }
        
        // Extract PNR and status from order (JsonNode)
        try {
            extractOrderDetails(response.getOrder(), builder);
        } catch (Exception e) {
            log.warn("Could not extract order details from response: {}", e.getMessage());
        }
        
        // Log the final built values for debugging
        log.info("Email builder - origin: {}, destination: {}, departureDate: {}, pnr: {}", 
                builder.toString());
        
        // Build passenger list from request
        List<BookingConfirmationEmail.PassengerDetail> passengers = new ArrayList<>();
        if (request.getTravellerInfo() != null) {
            for (TravellerInfo traveller : request.getTravellerInfo()) {
                passengers.add(BookingConfirmationEmail.PassengerDetail.builder()
                        .title(traveller.getTi())
                        .firstName(traveller.getfN())
                        .lastName(traveller.getlN())
                        .paxType(traveller.getPt())
                        .build());
            }
        }
        builder.passengers(passengers);
        
        // Set price info
        if (request.getPaymentInfos() != null && !request.getPaymentInfos().isEmpty()) {
            Number amount = request.getPaymentInfos().get(0).getAmount();
            if (amount != null) {
                builder.totalFare(BigDecimal.valueOf(amount.doubleValue()));
            }
        }
        builder.currency("INR");
        
        // Set recipient info from delivery/contact info
        if (request.getDeliveryInfo() != null && request.getDeliveryInfo().getEmails() != null 
                && !request.getDeliveryInfo().getEmails().isEmpty()) {
            builder.recipientEmail(request.getDeliveryInfo().getEmails().get(0));
        }
        if (request.getContactInfo() != null && request.getContactInfo().getEcn() != null) {
            builder.recipientName(request.getContactInfo().getEcn());
        } else if (!passengers.isEmpty()) {
            builder.recipientName(passengers.get(0).getFirstName() + " " + passengers.get(0).getLastName());
        }
        
        return builder.build();
    }
    
    private boolean extractFlightDetails(JsonNode itemInfos, BookingConfirmationEmail.BookingConfirmationEmailBuilder builder) {
        if (itemInfos == null) {
            log.warn("itemInfos is null");
            return false;
        }
        
        log.info("itemInfos structure: {}", itemInfos.toString());
        
        JsonNode air = itemInfos.get("AIR");
        if (air == null) {
            log.warn("AIR node is null in itemInfos");
            return false;
        }
        
        log.info("AIR node structure: {}", air.toString());
        
        JsonNode tripInfos = air.get("tripInfos");
        if (tripInfos == null) {
            log.warn("tripInfos is null");
            return false;
        }
        
        log.info("tripInfos type: isArray={}, isObject={}", tripInfos.isArray(), tripInfos.isObject());
        
        // tripInfos can be either an array or an object with keys like "ONWARD", "RETURN"
        JsonNode firstTrip = null;
        if (tripInfos.isArray() && !tripInfos.isEmpty()) {
            firstTrip = tripInfos.get(0);
        } else if (tripInfos.isObject()) {
            // Try ONWARD first, then get first available key
            if (tripInfos.has("ONWARD")) {
                JsonNode onward = tripInfos.get("ONWARD");
                if (onward.isArray() && !onward.isEmpty()) {
                    firstTrip = onward.get(0);
                }
            } else {
                // Get first available key
                var fields = tripInfos.fields();
                if (fields.hasNext()) {
                    JsonNode firstDirection = fields.next().getValue();
                    if (firstDirection.isArray() && !firstDirection.isEmpty()) {
                        firstTrip = firstDirection.get(0);
                    }
                }
            }
        }
        
        if (firstTrip == null) {
            log.warn("Could not extract first trip from tripInfos");
            return false;
        }
        
        log.info("firstTrip structure: {}", firstTrip.toString());
        
        JsonNode sI = firstTrip.get("sI");
        if (sI == null || !sI.isArray() || sI.isEmpty()) {
            log.warn("sI (segments) is null/empty or not an array");
            return false;
        }
        
        JsonNode firstSegment = sI.get(0);
        
        // Departure airport
        JsonNode da = firstSegment.get("da");
        if (da != null) {
            builder.origin(getTextValue(da, "code"));
        }
        
        // Arrival airport
        JsonNode aa = firstSegment.get("aa");
        if (aa != null) {
            builder.destination(getTextValue(aa, "code"));
        }
        
        // Departure and arrival times are at segment level (dt and at)
        String dt = getTextValue(firstSegment, "dt");
        String at = getTextValue(firstSegment, "at");
        
        if (dt != null) {
            // Format: "2026-01-02T00:05" -> date: "2026-01-02", time: "00:05"
            String[] dtParts = dt.split("T");
            if (dtParts.length == 2) {
                builder.departureDate(dtParts[0]);
                builder.departureTime(dtParts[1]);
            }
        }
        
        if (at != null) {
            // Format: "2026-01-02T01:40" -> date: "2026-01-02", time: "01:40"
            String[] atParts = at.split("T");
            if (atParts.length == 2) {
                builder.arrivalDate(atParts[0]);
                builder.arrivalTime(atParts[1]);
            }
        }
        
        // Flight designator
        JsonNode fD = firstSegment.get("fD");
        if (fD != null) {
            JsonNode aI = fD.get("aI");
            if (aI != null) {
                builder.airlineName(getTextValue(aI, "name"));
                builder.airlineCode(getTextValue(aI, "code"));
            }
            builder.flightNumber(getTextValue(fD, "fN"));
        }
        
        builder.cabinClass(getTextValue(firstSegment, "cabinClass"));
        
        return true;
    }
    
    private void extractOrderDetails(JsonNode order, BookingConfirmationEmail.BookingConfirmationEmailBuilder builder) {
        if (order == null) return;
        
        builder.pnr(getTextValue(order, "pnr"));
        builder.bookingStatus(getTextValue(order, "status"));
    }
    
    private void extractRouteFromSearchQuery(JsonNode searchQuery, BookingConfirmationEmail.BookingConfirmationEmailBuilder builder) {
        if (searchQuery == null) {
            log.warn("searchQuery is null");
            return;
        }
        
        log.info("searchQuery structure: {}", searchQuery.toString());
        
        // Try to extract route info from searchQuery.routeInfos
        JsonNode routeInfos = searchQuery.get("routeInfos");
        if (routeInfos != null && routeInfos.isArray() && !routeInfos.isEmpty()) {
            JsonNode firstRoute = routeInfos.get(0);
            if (firstRoute != null) {
                String fromCity = getTextValue(firstRoute, "fromCityOrAirport");
                String toCity = getTextValue(firstRoute, "toCityOrAirport");
                String travelDate = getTextValue(firstRoute, "travelDate");
                
                if (fromCity != null) {
                    builder.origin(fromCity);
                }
                if (toCity != null) {
                    builder.destination(toCity);
                }
                if (travelDate != null) {
                    builder.departureDate(travelDate);
                }
                
                log.info("Extracted from searchQuery - origin: {}, destination: {}, date: {}", 
                        fromCity, toCity, travelDate);
            }
        }
    }
    
    private String getTextValue(JsonNode node, String fieldName) {
        if (node == null) return null;
        JsonNode field = node.get(fieldName);
        return field != null && !field.isNull() ? field.asText() : null;
    }
    
    /**
     * Build a booking confirmation email from booking request and booking details response.
     * BookingDetailsResponse contains complete flight information including PNR, segments, etc.
     */
    public BookingConfirmationEmail buildFromBookingDetails(
            BookingRequest request, 
            BookingDetailsResponse detailsResponse) {
        
        BookingConfirmationEmail.BookingConfirmationEmailBuilder builder = BookingConfirmationEmail.builder();
        
        // Extract booking ID from order
        JsonNode order = detailsResponse.getOrder();
        if (order != null) {
            builder.bookingId(getTextValue(order, "bookingId"));
            builder.pnr(getTextValue(order, "pnr"));
            builder.bookingStatus(getTextValue(order, "status"));
            log.info("Extracted from order - bookingId: {}, pnr: {}, status: {}", 
                    getTextValue(order, "bookingId"), getTextValue(order, "pnr"), getTextValue(order, "status"));
        }
        
        // Extract flight details from itemInfos
        try {
            extractFlightDetails(detailsResponse.getItemInfos(), builder);
        } catch (Exception e) {
            log.warn("Could not extract flight details from booking details: {}", e.getMessage());
        }
        
        // Build passenger list from request
        List<BookingConfirmationEmail.PassengerDetail> passengers = new ArrayList<>();
        if (request.getTravellerInfo() != null) {
            for (TravellerInfo traveller : request.getTravellerInfo()) {
                passengers.add(BookingConfirmationEmail.PassengerDetail.builder()
                        .title(traveller.getTi())
                        .firstName(traveller.getfN())
                        .lastName(traveller.getlN())
                        .paxType(traveller.getPt())
                        .build());
            }
        }
        builder.passengers(passengers);
        
        // Set price info
//        if (request.getPaymentInfos() != null && !request.getPaymentInfos().isEmpty()) {
//            Number amount = request.getPaymentInfos().get(0).getAmount();
//            if (amount != null) {
//                builder.totalFare(BigDecimal.valueOf(amount.doubleValue()));
//            }
//        }
        builder.currency("INR");
        
        // Set recipient info
        if (request.getDeliveryInfo() != null && request.getDeliveryInfo().getEmails() != null 
                && !request.getDeliveryInfo().getEmails().isEmpty()) {
            builder.recipientEmail(request.getDeliveryInfo().getEmails().get(0));
        }
        if (request.getContactInfo() != null && request.getContactInfo().getEcn() != null) {
            builder.recipientName(request.getContactInfo().getEcn());
        } else if (!passengers.isEmpty()) {
            builder.recipientName(passengers.get(0).getFirstName() + " " + passengers.get(0).getLastName());
        }
        
        BookingConfirmationEmail email = builder.build();
        log.info("Built email - origin: {}, destination: {}, departureDate: {}, pnr: {}", 
                email.getOrigin(), email.getDestination(), email.getDepartureDate(), email.getPnr());
        
        return email;
    }
}
