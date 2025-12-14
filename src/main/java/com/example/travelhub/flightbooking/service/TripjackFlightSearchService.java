package com.example.travelhub.flightbooking.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import com.example.travelhub.config.CorsConfig;
import com.example.travelhub.flightbooking.models.FlightOptionDto;
import com.example.travelhub.flightbooking.models.FlightSearchRequest;
import com.example.travelhub.flightbooking.models.FlightSearchResultGroup;

import com.example.travelhub.flightbooking.models.TripjackMapper;
import com.example.travelhub.flightbooking.models.TripjackSearchRequest;
import com.example.travelhub.flightbooking.service.TripjackResponse.TripInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class TripjackFlightSearchService {

    private final CorsConfig corsConfig;

    private final WebClient webClient;
    private final TripjackMapper mapper;
    private final ObjectMapper objectMapper;
    private final AtomicLong tripjackHitCount = new AtomicLong(0);
    private final boolean logTripjack;

    public TripjackFlightSearchService(
            WebClient.Builder builder,
            TripjackMapper mapper,
            ObjectMapper objectMapper,
            @Value("${tripjack.base-url}") String baseUrl,
            @Value("${tripjack.api-key}") String apiKey,
            @Value("${travelhub.debug.log-tripjack:false}") boolean logTripjack,
            CorsConfig corsConfig) {

        // Increase buffer size to handle large responses
        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer
                .defaultCodecs()
                .maxInMemorySize(10 * 1024 * 1024)) // 10 MB
            .build();

        ConnectionProvider provider = ConnectionProvider.builder("tripjack")
                .maxConnections(50)
                .pendingAcquireTimeout(Duration.ofSeconds(60))
                .build();

        HttpClient httpClient = HttpClient.create(provider)
                .compress(true)
                .responseTimeout(Duration.ofSeconds(30));
            
        this.webClient = builder
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(strategies)
                .defaultHeader("apikey", apiKey)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
        this.mapper = mapper;
        this.objectMapper = objectMapper;
        this.corsConfig = corsConfig;
        this.logTripjack = logTripjack;
    }

 public Mono<FlightSearchResultGroup> search(FlightSearchRequest uiReq) {
    TripjackSearchRequest payload = mapper.toTripjackRequest(uiReq);

    if (logTripjack) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            System.out.println("====== Tripjack REQUEST Payload ======");
            System.out.println(json);
            System.out.println("======================================");
        } catch (JsonProcessingException e) {
            System.err.println("Failed to serialize request: " + e.getMessage());
        }
    }

    long tripjackHitNo = tripjackHitCount.incrementAndGet();
    final long tripjackStartNanos = System.nanoTime();

    return webClient.post()
            .uri("/fms/v1/air-search-all")
            .bodyValue(payload)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response.bodyToMono(String.class)
                    .doOnNext(errorBody -> System.err.println("Tripjack validation error: " + errorBody))
                    .map(errorBody -> new IllegalArgumentException("Tripjack API error: " + errorBody))
            )
            .bodyToMono(TripjackResponse.class)
            .doFinally(signalType -> {
                long tripjackMs = java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - tripjackStartNanos);
                System.out.println("Tripjack /fms/v1/air-search-all completed in " + tripjackMs + " ms (signal=" + signalType + ", hit #" + tripjackHitNo + ")");
            })
            .doOnNext(response -> {
                if (!logTripjack) {
                    return;
                }
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                    System.out.println("\n========== RAW TRIPJACK RESPONSE ==========");
                    System.out.println("Status: " + (response.getStatus() != null ? response.getStatus().isSuccess() : "null"));
                    System.out.println("SearchResult exists: " + (response.getSearchResult() != null));
                    
                    if (response.getSearchResult() != null) {
                        Map<String, List<TripInfo>> tripInfosMap = response.getSearchResult().getTripInfos();
                        System.out.println("TripInfos exists: " + (tripInfosMap != null));
                        
                        if (tripInfosMap != null) {
                            System.out.println("Available keys in tripInfos: " + tripInfosMap.keySet());
                            for (String key : tripInfosMap.keySet()) {
                                List<TripInfo> trips = tripInfosMap.get(key);
                                System.out.println("  - " + key + ": " + (trips != null ? trips.size() : 0) + " flights");
                            }
                        }
                    }

                    // Extract onward flights safely using map key
                    List<TripInfo> onward = response.getSearchResult() != null &&
                                            response.getSearchResult().getTripInfos() != null &&
                                            response.getSearchResult().getTripInfos().get("ONWARD") != null
                                            ? response.getSearchResult().getTripInfos().get("ONWARD")
                                            : Collections.<TripInfo>emptyList();

                    System.out.println("\n======= ONWARD FLIGHTS DETAILS =======");
                    System.out.println("Total onward flights: " + onward.size());
                    
                    if (!onward.isEmpty()) {
                        System.out.println("\nFirst flight sample:");
                        TripInfo firstFlight = onward.get(0);
                        
                        System.out.println("\n======= response.getSearchResult()=======");
                        // Create a wrapper object to match expected format
                        Map<String, Object> wrapper = new java.util.HashMap<>();
                        wrapper.put("searchResult", response.getSearchResult());
                        
                        // Write to file to avoid console truncation
                        try {
                            String jsonOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(wrapper);
                           System.out.println("jsonOutputdata"+jsonOutput);
                            java.nio.file.Files.writeString(
                                java.nio.file.Paths.get("tripjack-response-output.json"),
                                jsonOutput,
                                java.nio.charset.StandardCharsets.UTF_8
                            );
                            System.out.println("Full response written to: tripjack-response-output.json");
                            System.out.println("Response size: " + jsonOutput.length() + " characters");
                        } catch (Exception fileEx) {
                            System.err.println("Error writing to file: " + fileEx.getMessage());
                            // Fallback to console
                            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(wrapper));
                        }
                        System.out.println("  Segments: " + (firstFlight.getSegmentInfos() != null ? firstFlight.getSegmentInfos().size() : 0));
                        System.out.println("  Price list: " + (firstFlight.getTotalPriceList() != null ? firstFlight.getTotalPriceList().size() : 0));
                        
                        if (firstFlight.getSegmentInfos() != null && !firstFlight.getSegmentInfos().isEmpty()) {
                            var seg = firstFlight.getSegmentInfos().get(0);
                            System.out.println("  From: " + (seg.getDepartureAirport() != null ? seg.getDepartureAirport().getCode() : "null"));
                            System.out.println("  To: " + (seg.getArrivalAirport() != null ? seg.getArrivalAirport().getCode() : "null"));
                            System.out.println("  Departure: " + seg.getDepartureTime());
                            System.out.println("  Arrival: " + seg.getArrivalTime());
                        }
                        
                        if (firstFlight.getTotalPriceList() != null && !firstFlight.getTotalPriceList().isEmpty()) {
                            Double price = firstFlight.getTotalPriceList().get(0).getTotalPrice();
                            System.out.println("  Price: " + price);
                        }
                    }
                    
                    System.out.println("\nComplete ONWARD data (JSON):");
                    System.out.println(mapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(onward));
                    System.out.println("==========================================\n");

                } catch (Exception e) {
                    System.err.println("Error logging response:");
                    e.printStackTrace();
                }
            })
            .map(this::transformToFlightSearchResultGroup);
}
    private FlightSearchResultGroup transformToFlightSearchResultGroup(TripjackResponse tripjackResponse) {
        FlightSearchResultGroup result = new FlightSearchResultGroup();
        
        if (tripjackResponse.getSearchResult() == null || 
            tripjackResponse.getSearchResult().getTripInfos() == null) {
            result.setOnward(new ArrayList<>());
            result.setReturns(new ArrayList<>());
            result.setCombos(new ArrayList<>());
            return result;
        }

        Map<String, List<TripjackResponse.TripInfo>> tripInfosMap = 
            tripjackResponse.getSearchResult().getTripInfos();
        
        // Extract ONWARD flights
        if (tripInfosMap.containsKey("ONWARD")) {
            result.setOnward(convertToFlightOptions(tripInfosMap.get("ONWARD")));
        } else {
            result.setOnward(new ArrayList<>());
        }
        
        // Extract RETURN flights if present
        if (tripInfosMap.containsKey("RETURN")) {
            result.setReturns(convertToFlightOptions(tripInfosMap.get("RETURN")));
        } else {
            result.setReturns(new ArrayList<>());
        }
        
        // Combos are usually combinations of onward + return
        result.setCombos(new ArrayList<>());

        if (logTripjack) {
            System.out.println("\n====== TRANSFORMATION SUMMARY ======");
            System.out.println("Onward flights converted: " + result.getOnward().size());
            System.out.println("Return flights converted: " + result.getReturns().size());

            if (!result.getOnward().isEmpty()) {
                FlightOptionDto firstOption = result.getOnward().get(0);
                System.out.println("\nFirst converted flight:");
                System.out.println("  From: " + firstOption.getFrom());
                System.out.println("  To: " + firstOption.getTo());
                System.out.println("  Departure: " + firstOption.getDeparture());
                System.out.println("  Arrival: " + firstOption.getArrival());
                System.out.println("  Duration: " + firstOption.getTotalDurationMinutes() + " mins");
                System.out.println("  Stops: " + firstOption.getStops());
                System.out.println("  Price: " + firstOption.getTotalFare());
                System.out.println("  Airline: " + firstOption.getMarketingAirlineName() + " (" + firstOption.getMarketingAirlineCode() + ")");
                System.out.println("  Flight#: " + firstOption.getFlightNumber());
                System.out.println("  Baggage Check-in: " + firstOption.getBaggageCheckIn());
                System.out.println("  Baggage Cabin: " + firstOption.getBaggageCabin());
            }
            System.out.println("====================================\n");
        }
        
        return result;
    }

    private List<FlightOptionDto> convertToFlightOptions(List<TripjackResponse.TripInfo> tripInfos) {
        List<FlightOptionDto> options = new ArrayList<>();
        
        if (tripInfos == null) {
            return options;
        }
        
        // TODO: TEMPORARY - Send only 1 record to UI for testing
        // Comment out to send all records
  //      int recordLimit = 1;
        int recordCount = 0;
        
        for (TripjackResponse.TripInfo tripInfo : tripInfos) {
            // TODO: TEMPORARY - Limit to 1 record
//            if (recordCount >= recordLimit) {
//                break;
//            }
            FlightOptionDto option = new FlightOptionDto();
            
            if (tripInfo.getSegmentInfos() != null && !tripInfo.getSegmentInfos().isEmpty()) {
                List<FlightOptionDto.SegmentDto> uiSegments = new ArrayList<>();
                for (TripjackResponse.SegmentInfo segment : tripInfo.getSegmentInfos()) {
                    FlightOptionDto.SegmentDto uiSeg = new FlightOptionDto.SegmentDto();

                    if (segment.getDepartureAirport() != null) {
                        uiSeg.setFrom(segment.getDepartureAirport().getCode());
                    }
                    if (segment.getArrivalAirport() != null) {
                        uiSeg.setTo(segment.getArrivalAirport().getCode());
                    }
                    uiSeg.setDeparture(segment.getDepartureTime());
                    uiSeg.setArrival(segment.getArrivalTime());
                    uiSeg.setDuration(segment.getDuration());

                    if (segment.getFlightDetails() != null && segment.getFlightDetails().getAirlineInfo() != null) {
                        uiSeg.setAirlineCode(segment.getFlightDetails().getAirlineInfo().getCode());
                        uiSeg.setAirlineName(segment.getFlightDetails().getAirlineInfo().getName());
                    }
                    if (segment.getFlightDetails() != null) {
                        uiSeg.setFlightNumber(segment.getFlightDetails().getFlightNumber());
                    }

                    uiSegments.add(uiSeg);
                }
                option.setSegments(uiSegments);

                TripjackResponse.SegmentInfo firstSegment = tripInfo.getSegmentInfos().get(0);
                TripjackResponse.SegmentInfo lastSegment = 
                    tripInfo.getSegmentInfos().get(tripInfo.getSegmentInfos().size() - 1);
                
                // Map departure and arrival airports
                if (firstSegment.getDepartureAirport() != null) {
                    option.setFrom(firstSegment.getDepartureAirport().getCode());
                }
                if (lastSegment.getArrivalAirport() != null) {
                    option.setTo(lastSegment.getArrivalAirport().getCode());
                }
                
                // Map departure and arrival times
                String departure = firstSegment.getDepartureTime();
                if (departure != null) {
                    option.setDeparture(departure);
                }
                String arrival = lastSegment.getArrivalTime();
                if (arrival != null) {
                    option.setArrival(arrival);
                }
                
                // Calculate TOTAL duration across all segments + connection times
                int totalDuration = 0;
                for (TripjackResponse.SegmentInfo segment : tripInfo.getSegmentInfos()) {
                    if (segment.getDuration() != null) {
                        totalDuration += segment.getDuration();
                    }
                    // Add connection time if present
                    if (segment.getConnectionTime() != null) {
                        totalDuration += segment.getConnectionTime();
                    }
                }
                option.setTotalDurationMinutes(totalDuration);
                
                // Set number of stops (segments - 1)
                option.setStops(tripInfo.getSegmentInfos().size() - 1);
                
                // Get price
                if (tripInfo.getTotalPriceList() != null && !tripInfo.getTotalPriceList().isEmpty()) {
                    TripjackResponse.PriceInfo priceInfo = tripInfo.getTotalPriceList().get(0);
                    Double price = priceInfo.getTotalPrice();
                    if (price != null) {
                        option.setTotalFare(java.math.BigDecimal.valueOf(price));
                    }
                    option.setCurrency("INR");
                    
                    // Set price ID
                    if (priceInfo.getId() != null) {
                        option.setPriceId(priceInfo.getId());
                    }
                    
                    // Set refundable status
                    option.setRefundable(priceInfo.getIcca() != null ? !priceInfo.getIcca() : false);
                    
                    // Extract baggage info from fd
                    if (priceInfo.getFd() != null && priceInfo.getFd().containsKey("ADULT")) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> adult = (Map<String, Object>) priceInfo.getFd().get("ADULT");
                        if (adult != null && adult.containsKey("bI")) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> baggage = (Map<String, Object>) adult.get("bI");
                            if (baggage != null) {
                                option.setBaggageCheckIn((String) baggage.get("iB"));
                                option.setBaggageCabin((String) baggage.get("cB"));
                            }
                        }
                        // Get cabin class and booking class
                        if (adult.containsKey("cc")) {
                            option.setCabinClass((String) adult.get("cc"));
                        }
                        if (adult.containsKey("cB")) {
                            option.setBookingClass((String) adult.get("cB"));
                        }
                        // Get seats remaining
                        if (adult.containsKey("sR")) {
                            Object sR = adult.get("sR");
                            if (sR instanceof Number) {
                                option.setSeatsRemaining(((Number) sR).intValue());
                            }
                        }
                    }
                }
                
                // Get airline info
                if (firstSegment.getFlightDetails() != null && 
                    firstSegment.getFlightDetails().getAirlineInfo() != null) {
                    option.setMarketingAirlineName(firstSegment.getFlightDetails().getAirlineInfo().getName());
                    option.setMarketingAirlineCode(firstSegment.getFlightDetails().getAirlineInfo().getCode());
                }
                
                // Get flight number
                if (firstSegment.getFlightDetails() != null && 
                    firstSegment.getFlightDetails().getFlightNumber() != null) {
                    option.setFlightNumber(firstSegment.getFlightDetails().getFlightNumber());
                }
            }
            
            options.add(option);
            
            // TODO: TEMPORARY - Increment counter for record limit
            recordCount++;
        }
        
        return options;
    }
}