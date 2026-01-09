package com.example.travelhub.flightbooking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.stereotype.Service;

import com.example.travelhub.flightbooking.models.FlightSearchRequest;
import com.example.travelhub.flightbooking.service.TripjackResponse.*;

/**
 * Mock data service that generates TripJack-format responses for testing.
 * Use this as a backup when TripJack test server is unavailable.
 */
@Service
public class MockTripjackDataService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final Random random = new Random();

    // Airline data
    private static final List<AirlineData> AIRLINES = Arrays.asList(
        new AirlineData("AI", "Air India", false),
        new AirlineData("6E", "IndiGo", true),
        new AirlineData("UK", "Vistara", false),
        new AirlineData("SG", "SpiceJet", true),
        new AirlineData("G8", "Go First", true),
        new AirlineData("QP", "Akasa Air", true),
        new AirlineData("I5", "AirAsia India", true),
        new AirlineData("EK", "Emirates", false),
        new AirlineData("QR", "Qatar Airways", false),
        new AirlineData("SQ", "Singapore Airlines", false),
        new AirlineData("LH", "Lufthansa", false),
        new AirlineData("BA", "British Airways", false)
    );

    // Airport data
    private static final Map<String, AirportData> AIRPORTS = new HashMap<>();
    static {
        // Indian Airports
        AIRPORTS.put("DEL", new AirportData("DEL", "Indira Gandhi International Airport", "Delhi", "DEL", "India", "IN", "T3"));
        AIRPORTS.put("BOM", new AirportData("BOM", "Chhatrapati Shivaji Maharaj International Airport", "Mumbai", "BOM", "India", "IN", "T2"));
        AIRPORTS.put("BLR", new AirportData("BLR", "Kempegowda International Airport", "Bangalore", "BLR", "India", "IN", "T1"));
        AIRPORTS.put("MAA", new AirportData("MAA", "Chennai International Airport", "Chennai", "MAA", "India", "IN", "T1"));
        AIRPORTS.put("CCU", new AirportData("CCU", "Netaji Subhas Chandra Bose International Airport", "Kolkata", "CCU", "India", "IN", "T2"));
        AIRPORTS.put("HYD", new AirportData("HYD", "Rajiv Gandhi International Airport", "Hyderabad", "HYD", "India", "IN", "T1"));
        AIRPORTS.put("COK", new AirportData("COK", "Cochin International Airport", "Kochi", "COK", "India", "IN", "T3"));
        AIRPORTS.put("GOI", new AirportData("GOI", "Goa International Airport", "Goa", "GOI", "India", "IN", "T1"));
        AIRPORTS.put("PNQ", new AirportData("PNQ", "Pune Airport", "Pune", "PNQ", "India", "IN", "T1"));
        AIRPORTS.put("AMD", new AirportData("AMD", "Sardar Vallabhbhai Patel International Airport", "Ahmedabad", "AMD", "India", "IN", "T1"));
        AIRPORTS.put("JAI", new AirportData("JAI", "Jaipur International Airport", "Jaipur", "JAI", "India", "IN", "T2"));
        
        // International Airports
        AIRPORTS.put("DXB", new AirportData("DXB", "Dubai International Airport", "Dubai", "DXB", "United Arab Emirates", "AE", "T3"));
        AIRPORTS.put("SIN", new AirportData("SIN", "Singapore Changi Airport", "Singapore", "SIN", "Singapore", "SG", "T3"));
        AIRPORTS.put("LHR", new AirportData("LHR", "London Heathrow Airport", "London", "LON", "United Kingdom", "GB", "T5"));
        AIRPORTS.put("JFK", new AirportData("JFK", "John F Kennedy International Airport", "New York", "NYC", "United States", "US", "T4"));
        AIRPORTS.put("DOH", new AirportData("DOH", "Hamad International Airport", "Doha", "DOH", "Qatar", "QA", "T1"));
        AIRPORTS.put("FRA", new AirportData("FRA", "Frankfurt Airport", "Frankfurt", "FRA", "Germany", "DE", "T1"));
        AIRPORTS.put("BKK", new AirportData("BKK", "Suvarnabhumi Airport", "Bangkok", "BKK", "Thailand", "TH", "T1"));
        AIRPORTS.put("KUL", new AirportData("KUL", "Kuala Lumpur International Airport", "Kuala Lumpur", "KUL", "Malaysia", "MY", "T1"));
    }

    // Flight duration estimates (in minutes) for common routes
    private static final Map<String, Integer> ROUTE_DURATIONS = new HashMap<>();
    static {
        // Domestic routes
        ROUTE_DURATIONS.put("DEL-BOM", 130);
        ROUTE_DURATIONS.put("DEL-BLR", 165);
        ROUTE_DURATIONS.put("DEL-MAA", 175);
        ROUTE_DURATIONS.put("DEL-CCU", 130);
        ROUTE_DURATIONS.put("DEL-HYD", 140);
        ROUTE_DURATIONS.put("BOM-BLR", 95);
        ROUTE_DURATIONS.put("BOM-DEL", 130);
        ROUTE_DURATIONS.put("BOM-MAA", 110);
        ROUTE_DURATIONS.put("BOM-HYD", 85);
        ROUTE_DURATIONS.put("BLR-DEL", 165);
        ROUTE_DURATIONS.put("BLR-BOM", 95);
        ROUTE_DURATIONS.put("BLR-MAA", 55);
        ROUTE_DURATIONS.put("MAA-DEL", 175);
        ROUTE_DURATIONS.put("MAA-BOM", 110);
        ROUTE_DURATIONS.put("HYD-DEL", 140);
        ROUTE_DURATIONS.put("HYD-BOM", 85);
        ROUTE_DURATIONS.put("CCU-DEL", 130);
        ROUTE_DURATIONS.put("GOI-DEL", 150);
        ROUTE_DURATIONS.put("GOI-BOM", 75);
        
        // International routes
        ROUTE_DURATIONS.put("DEL-DXB", 210);
        ROUTE_DURATIONS.put("BOM-DXB", 180);
        ROUTE_DURATIONS.put("DEL-SIN", 330);
        ROUTE_DURATIONS.put("BOM-SIN", 300);
        ROUTE_DURATIONS.put("DEL-LHR", 540);
        ROUTE_DURATIONS.put("BOM-LHR", 560);
        ROUTE_DURATIONS.put("DEL-JFK", 900);
        ROUTE_DURATIONS.put("BOM-JFK", 960);
        ROUTE_DURATIONS.put("DEL-DOH", 240);
        ROUTE_DURATIONS.put("DEL-BKK", 240);
        ROUTE_DURATIONS.put("BOM-BKK", 270);
    }

    /**
     * Generate mock TripJack response for flight search
     */
    public TripjackResponse generateMockResponse(FlightSearchRequest request) {
        TripjackResponse response = new TripjackResponse();
        
        // Set status
        Status status = new Status();
        status.setSuccess(true);
        status.setHttpStatus(200);
        response.setStatus(status);
        
        // Set search query
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setCabinClass(request.getTravelClass() != null ? request.getTravelClass().toUpperCase() : "ECONOMY");
        response.setSearchQuery(searchQuery);
        
        // Generate search results
        SearchResult searchResult = new SearchResult();
        Map<String, List<TripInfo>> tripInfosMap = new HashMap<>();
        
        String tripType = request.getTripType();
        
        if ("one-way".equalsIgnoreCase(tripType)) {
            List<TripInfo> onwardFlights = generateFlightsForRoute(
                request.getOrigin(),
                request.getDestination(),
                request.getDepartureDate(),
                request.getTravelClass(),
                request.getPassengers()
            );
            tripInfosMap.put("ONWARD", onwardFlights);
            
        } else if ("round-trip".equalsIgnoreCase(tripType)) {
            List<TripInfo> onwardFlights = generateFlightsForRoute(
                request.getOrigin(),
                request.getDestination(),
                request.getDepartureDate(),
                request.getTravelClass(),
                request.getPassengers()
            );
            tripInfosMap.put("ONWARD", onwardFlights);
            
            List<TripInfo> returnFlights = generateFlightsForRoute(
                request.getDestination(),
                request.getOrigin(),
                request.getReturnDate(),
                request.getTravelClass(),
                request.getPassengers()
            );
            tripInfosMap.put("RETURN", returnFlights);
            
        } else if ("multi-city".equalsIgnoreCase(tripType) && request.getMultiCityLegs() != null) {
            // For multi-city, generate COMBO flights
            List<TripInfo> comboFlights = new ArrayList<>();
            for (var leg : request.getMultiCityLegs()) {
                List<TripInfo> legFlights = generateFlightsForRoute(
                    leg.getOrigin(),
                    leg.getDestination(),
                    leg.getTravelDate(),
                    request.getTravelClass(),
                    request.getPassengers()
                );
                comboFlights.addAll(legFlights);
            }
            tripInfosMap.put("COMBO", comboFlights);
        }
        
        searchResult.setTripInfos(tripInfosMap);
        response.setSearchResult(searchResult);
        
        return response;
    }

    private List<TripInfo> generateFlightsForRoute(String origin, String destination, 
            LocalDate travelDate, String cabinClass, com.example.travelhub.flightbooking.models.Passengers passengers) {
        
        List<TripInfo> flights = new ArrayList<>();
        
        // Generate 8-15 flight options
        int numFlights = 8 + random.nextInt(8);
        
        // Departure times spread throughout the day
        int[] departureHours = {6, 7, 8, 9, 10, 11, 12, 14, 15, 16, 17, 18, 19, 20, 21, 22};
        
        for (int i = 0; i < numFlights && i < departureHours.length; i++) {
            boolean isDirect = random.nextDouble() < 0.6; // 60% direct flights
            
            if (isDirect) {
                flights.add(generateDirectFlight(origin, destination, travelDate, 
                    departureHours[i], cabinClass, passengers, i));
            } else {
                flights.add(generateConnectingFlight(origin, destination, travelDate, 
                    departureHours[i], cabinClass, passengers, i));
            }
        }
        
        return flights;
    }

    private TripInfo generateDirectFlight(String origin, String destination, LocalDate travelDate,
            int departureHour, String cabinClass, com.example.travelhub.flightbooking.models.Passengers passengers, int index) {
        
        TripInfo tripInfo = new TripInfo();
        List<SegmentInfo> segments = new ArrayList<>();
        
        // Get duration for route
        int duration = getRouteDuration(origin, destination);
        int departureMinute = random.nextInt(60);
        
        LocalDateTime departureTime = travelDate.atTime(departureHour, departureMinute);
        LocalDateTime arrivalTime = departureTime.plusMinutes(duration);
        
        // Select airline
        AirlineData airline = selectAirlineForRoute(origin, destination);
        String flightNumber = String.valueOf(100 + random.nextInt(900));
        String segmentId = String.valueOf(100 + index);
        
        SegmentInfo segment = createSegment(
            segmentId, origin, destination, departureTime, arrivalTime, 
            duration, 0, airline, flightNumber
        );
        segments.add(segment);
        
        tripInfo.setSegmentInfos(segments);
        tripInfo.setAirFlowType("SEARCH");
        tripInfo.setIpm("");
        tripInfo.setIssf(false);
        
        // Generate price info
        List<PriceInfo> priceList = new ArrayList<>();
        priceList.add(generatePriceInfo(origin, destination, duration, cabinClass, 
            passengers, airline, segmentId, true, index));
        tripInfo.setTotalPriceList(priceList);
        
        return tripInfo;
    }

    private TripInfo generateConnectingFlight(String origin, String destination, LocalDate travelDate,
            int departureHour, String cabinClass, com.example.travelhub.flightbooking.models.Passengers passengers, int index) {
        
        TripInfo tripInfo = new TripInfo();
        List<SegmentInfo> segments = new ArrayList<>();
        
        // Find a connecting city
        String connectingCity = findConnectingCity(origin, destination);
        
        // First segment
        int duration1 = getRouteDuration(origin, connectingCity);
        int departureMinute = random.nextInt(60);
        LocalDateTime departureTime1 = travelDate.atTime(departureHour, departureMinute);
        LocalDateTime arrivalTime1 = departureTime1.plusMinutes(duration1);
        
        AirlineData airline = selectAirlineForRoute(origin, destination);
        String flightNumber1 = String.valueOf(100 + random.nextInt(900));
        String segmentId1 = String.valueOf(200 + index * 2);
        
        SegmentInfo segment1 = createSegment(
            segmentId1, origin, connectingCity, departureTime1, arrivalTime1,
            duration1, 0, airline, flightNumber1
        );
        segments.add(segment1);
        
        // Layover time (60-180 minutes)
        int layoverTime = 60 + random.nextInt(121);
        
        // Second segment
        int duration2 = getRouteDuration(connectingCity, destination);
        LocalDateTime departureTime2 = arrivalTime1.plusMinutes(layoverTime);
        LocalDateTime arrivalTime2 = departureTime2.plusMinutes(duration2);
        
        String flightNumber2 = String.valueOf(100 + random.nextInt(900));
        String segmentId2 = String.valueOf(201 + index * 2);
        
        SegmentInfo segment2 = createSegment(
            segmentId2, connectingCity, destination, departureTime2, arrivalTime2,
            duration2, layoverTime, airline, flightNumber2
        );
        segments.add(segment2);
        
        tripInfo.setSegmentInfos(segments);
        tripInfo.setAirFlowType("SEARCH");
        tripInfo.setIpm("");
        tripInfo.setIssf(false);
        
        // Generate price info (connecting flights are usually cheaper)
        List<PriceInfo> priceList = new ArrayList<>();
        priceList.add(generatePriceInfo(origin, destination, duration1 + duration2 + layoverTime, 
            cabinClass, passengers, airline, segmentId1, false, index));
        tripInfo.setTotalPriceList(priceList);
        
        return tripInfo;
    }

    private SegmentInfo createSegment(String id, String origin, String destination,
            LocalDateTime departureTime, LocalDateTime arrivalTime, int duration,
            int connectionTime, AirlineData airline, String flightNumber) {
        
        SegmentInfo segment = new SegmentInfo();
        segment.setId(id);
        segment.setDepartureAirport(createAirportInfo(origin));
        segment.setArrivalAirport(createAirportInfo(destination));
        segment.setDepartureTime(departureTime.format(DATE_TIME_FORMATTER));
        segment.setArrivalTime(arrivalTime.format(DATE_TIME_FORMATTER));
        segment.setDuration(duration);
        segment.setStops(0);
        segment.setConnectionTime(connectionTime > 0 ? connectionTime : null);
        segment.setSegmentNumber(0);
        segment.setIand(isInternationalRoute(origin, destination));
        segment.setIsRs(false);
        segment.setStopOvers(new ArrayList<>());
        
        FlightDetails flightDetails = new FlightDetails();
        AirlineInfo airlineInfo = new AirlineInfo();
        airlineInfo.setCode(airline.code);
        airlineInfo.setName(airline.name);
        airlineInfo.setIsLcc(airline.isLcc);
        flightDetails.setAirlineInfo(airlineInfo);
        flightDetails.setFlightNumber(flightNumber);
        flightDetails.setEquipmentType(getEquipmentType(duration));
        segment.setFlightDetails(flightDetails);
        
        return segment;
    }

    private AirportInfo createAirportInfo(String code) {
        AirportInfo airportInfo = new AirportInfo();
        AirportData data = AIRPORTS.get(code);
        
        if (data != null) {
            airportInfo.setCode(data.code);
            airportInfo.setName(data.name);
            airportInfo.setCity(data.city);
            airportInfo.setCityCode(data.cityCode);
            airportInfo.setCountry(data.country);
            airportInfo.setCountryCode(data.countryCode);
            airportInfo.setTerminal(data.terminal);
        } else {
            // Fallback for unknown airports
            airportInfo.setCode(code);
            airportInfo.setName(code + " Airport");
            airportInfo.setCity(code);
            airportInfo.setCityCode(code);
            airportInfo.setCountry("Unknown");
            airportInfo.setCountryCode("XX");
            airportInfo.setTerminal("T1");
        }
        
        return airportInfo;
    }

    private PriceInfo generatePriceInfo(String origin, String destination, int totalDuration,
            String cabinClass, com.example.travelhub.flightbooking.models.Passengers passengers,
            AirlineData airline, String segmentId, boolean isDirect, int index) {
        
        PriceInfo priceInfo = new PriceInfo();
        
        // Calculate base fare based on route and class
        double baseFare = calculateBaseFare(origin, destination, cabinClass, isDirect);
        double taxes = baseFare * 0.18; // 18% taxes
        double totalFare = baseFare + taxes;
        
        // Create fare details map
        Map<String, Object> fd = new HashMap<>();
        
        // Adult fare
        Map<String, Object> adultFare = new HashMap<>();
        
        // Fare components
        Map<String, Object> fC = new HashMap<>();
        fC.put("BF", baseFare);
        fC.put("TAF", taxes);
        fC.put("NF", totalFare);
        fC.put("TF", totalFare);
        adultFare.put("fC", fC);
        
        // Additional fare components
        Map<String, Object> afC = new HashMap<>();
        Map<String, Object> tafBreakdown = new HashMap<>();
        tafBreakdown.put("YQ", taxes * 0.6);
        tafBreakdown.put("OT", taxes * 0.35);
        tafBreakdown.put("MF", taxes * 0.03);
        tafBreakdown.put("MFT", taxes * 0.02);
        afC.put("TAF", tafBreakdown);
        adultFare.put("afC", afC);
        
        // Baggage info
        Map<String, Object> bI = new HashMap<>();
        bI.put("iB", getBaggageAllowance(cabinClass, isDirect)); // Check-in baggage
        bI.put("cB", "7 Kg"); // Cabin baggage
        adultFare.put("bI", bI);
        
        // Other details
        adultFare.put("sR", 5 + random.nextInt(10)); // Seats remaining
        adultFare.put("rT", 0);
        adultFare.put("cc", cabinClass != null ? cabinClass.toUpperCase() : "ECONOMY");
        adultFare.put("cB", getBookingClass(cabinClass));
        adultFare.put("fB", generateFareBasis(cabinClass, airline.code));
        
        fd.put("ADULT", adultFare);
        
        // Add child fare if applicable
        if (passengers != null && passengers.getChildren() > 0) {
            Map<String, Object> childFare = new HashMap<>(adultFare);
            Map<String, Object> childFC = new HashMap<>(fC);
            childFC.put("BF", baseFare * 0.75);
            childFC.put("TAF", taxes * 0.75);
            childFC.put("NF", totalFare * 0.75);
            childFC.put("TF", totalFare * 0.75);
            childFare.put("fC", childFC);
            fd.put("CHILD", childFare);
        }
        
        // Add infant fare if applicable
        if (passengers != null && passengers.getInfants() > 0) {
            Map<String, Object> infantFare = new HashMap<>(adultFare);
            Map<String, Object> infantFC = new HashMap<>(fC);
            infantFC.put("BF", baseFare * 0.1);
            infantFC.put("TAF", taxes * 0.1);
            infantFC.put("NF", totalFare * 0.1);
            infantFC.put("TF", totalFare * 0.1);
            infantFare.put("fC", infantFC);
            Map<String, Object> infantBI = new HashMap<>();
            infantBI.put("iB", "0 Kg");
            infantBI.put("cB", "0 Kg");
            infantFare.put("bI", infantBI);
            fd.put("INFANT", infantFare);
        }
        
        priceInfo.setFd(fd);
        priceInfo.setFareIdentifier("PUBLISHED");
        priceInfo.setId(generatePriceId(origin, destination, airline.code, segmentId, index));
        priceInfo.setMsri(new ArrayList<>());
        priceInfo.setMessages(new ArrayList<>());
        priceInfo.setIcca(random.nextDouble() < 0.3); // 30% non-refundable
        
        // TAI (additional info with baggage per segment)
        Map<String, Object> tai = new HashMap<>();
        Map<String, Object> tbi = new HashMap<>();
        List<Map<String, Object>> segmentBaggage = new ArrayList<>();
        Map<String, Object> segBag = new HashMap<>();
        Map<String, Object> adultBag = new HashMap<>();
        adultBag.put("iB", getBaggageAllowance(cabinClass, isDirect));
        adultBag.put("cB", "7 Kg");
        segBag.put("ADULT", adultBag);
        segmentBaggage.add(segBag);
        tbi.put(segmentId, segmentBaggage);
        tai.put("tbi", tbi);
        priceInfo.setTai(tai);
        
        return priceInfo;
    }

    private double calculateBaseFare(String origin, String destination, String cabinClass, boolean isDirect) {
        // Base fare calculation
        double baseFare = 3000; // Minimum base fare
        
        // Add distance-based component
        int duration = getRouteDuration(origin, destination);
        baseFare += duration * 15; // Rs 15 per minute of flight
        
        // International routes are more expensive
        if (isInternationalRoute(origin, destination)) {
            baseFare *= 2.5;
        }
        
        // Direct flights premium
        if (isDirect) {
            baseFare *= 1.15;
        }
        
        // Cabin class multiplier
        if (cabinClass != null) {
            switch (cabinClass.toUpperCase()) {
                case "BUSINESS":
                    baseFare *= 3.5;
                    break;
                case "FIRST":
                    baseFare *= 6.0;
                    break;
                case "PREMIUM_ECONOMY":
                    baseFare *= 1.8;
                    break;
            }
        }
        
        // Add some randomness (+/- 20%)
        baseFare *= (0.8 + random.nextDouble() * 0.4);
        
        return Math.round(baseFare * 100.0) / 100.0;
    }

    private int getRouteDuration(String origin, String destination) {
        String routeKey = origin + "-" + destination;
        Integer duration = ROUTE_DURATIONS.get(routeKey);
        
        if (duration != null) {
            return duration;
        }
        
        // Check reverse route
        String reverseKey = destination + "-" + origin;
        duration = ROUTE_DURATIONS.get(reverseKey);
        if (duration != null) {
            return duration;
        }
        
        // Default duration based on whether it's international
        if (isInternationalRoute(origin, destination)) {
            return 300 + random.nextInt(300); // 5-10 hours
        }
        return 90 + random.nextInt(90); // 1.5-3 hours
    }

    private boolean isInternationalRoute(String origin, String destination) {
        AirportData originAirport = AIRPORTS.get(origin);
        AirportData destAirport = AIRPORTS.get(destination);
        
        if (originAirport == null || destAirport == null) {
            return false;
        }
        
        return !originAirport.countryCode.equals(destAirport.countryCode);
    }

    private AirlineData selectAirlineForRoute(String origin, String destination) {
        boolean isInternational = isInternationalRoute(origin, destination);
        
        List<AirlineData> eligibleAirlines = new ArrayList<>();
        for (AirlineData airline : AIRLINES) {
            if (isInternational) {
                // For international routes, prefer full-service carriers
                if (!airline.isLcc || random.nextDouble() < 0.3) {
                    eligibleAirlines.add(airline);
                }
            } else {
                // For domestic routes, include all Indian carriers
                if (airline.code.equals("AI") || airline.code.equals("6E") || 
                    airline.code.equals("UK") || airline.code.equals("SG") ||
                    airline.code.equals("G8") || airline.code.equals("QP") ||
                    airline.code.equals("I5")) {
                    eligibleAirlines.add(airline);
                }
            }
        }
        
        if (eligibleAirlines.isEmpty()) {
            return AIRLINES.get(0);
        }
        
        return eligibleAirlines.get(random.nextInt(eligibleAirlines.size()));
    }

    private String findConnectingCity(String origin, String destination) {
        // Common connecting hubs
        List<String> hubs = Arrays.asList("DEL", "BOM", "BLR", "HYD", "DXB", "DOH", "SIN");
        
        for (String hub : hubs) {
            if (!hub.equals(origin) && !hub.equals(destination)) {
                return hub;
            }
        }
        
        return "DEL"; // Default hub
    }

    private String getEquipmentType(int duration) {
        if (duration > 300) {
            return random.nextBoolean() ? "Boeing 787" : "Airbus A350";
        } else if (duration > 150) {
            return random.nextBoolean() ? "Boeing 737" : "Airbus A320";
        } else {
            return random.nextBoolean() ? "Airbus A320neo" : "ATR 72";
        }
    }

    private String getBaggageAllowance(String cabinClass, boolean isDirect) {
        if (cabinClass == null) {
            return "15 Kg";
        }
        
        switch (cabinClass.toUpperCase()) {
            case "BUSINESS":
                return "30 Kg";
            case "FIRST":
                return "40 Kg";
            case "PREMIUM_ECONOMY":
                return "25 Kg";
            default:
                return isDirect ? "15 Kg" : "20 Kg";
        }
    }

    private String getBookingClass(String cabinClass) {
        if (cabinClass == null) {
            return "E";
        }
        
        switch (cabinClass.toUpperCase()) {
            case "BUSINESS":
                return "J";
            case "FIRST":
                return "F";
            case "PREMIUM_ECONOMY":
                return "W";
            default:
                return "E";
        }
    }

    private String generateFareBasis(String cabinClass, String airlineCode) {
        String prefix = "E";
        if (cabinClass != null) {
            switch (cabinClass.toUpperCase()) {
                case "BUSINESS":
                    prefix = "J";
                    break;
                case "FIRST":
                    prefix = "F";
                    break;
                case "PREMIUM_ECONOMY":
                    prefix = "W";
                    break;
            }
        }
        return prefix + "L0" + airlineCode + random.nextInt(100);
    }

    private String generatePriceId(String origin, String destination, String airlineCode, 
            String segmentId, int index) {
        return String.format("%d-%d-%d-%d-%s%s%s%d~%d",
            random.nextInt(20), random.nextInt(20), random.nextInt(5), random.nextInt(15),
            origin, destination, airlineCode, index,
            System.currentTimeMillis() + random.nextInt(1000000));
    }

    // Helper classes
    private static class AirlineData {
        String code;
        String name;
        boolean isLcc;
        
        AirlineData(String code, String name, boolean isLcc) {
            this.code = code;
            this.name = name;
            this.isLcc = isLcc;
        }
    }

    private static class AirportData {
        String code;
        String name;
        String city;
        String cityCode;
        String country;
        String countryCode;
        String terminal;
        
        AirportData(String code, String name, String city, String cityCode, 
                String country, String countryCode, String terminal) {
            this.code = code;
            this.name = name;
            this.city = city;
            this.cityCode = cityCode;
            this.country = country;
            this.countryCode = countryCode;
            this.terminal = terminal;
        }
    }
}
