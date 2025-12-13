package com.example.travelhub.flightbooking.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelhub.flightbooking.models.FlightSearchRequest;
import com.example.travelhub.flightbooking.models.FlightSearchResultGroup;
import com.example.travelhub.flightbooking.service.TripjackFlightSearchService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/flights")
public class FlightSearchController {

    private final TripjackFlightSearchService searchService;

    public FlightSearchController(TripjackFlightSearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/search")
    public Mono<FlightSearchResultGroup> search(@RequestBody FlightSearchRequest request) throws JsonProcessingException {
        System.out.println("Incoming UI request: " + request);

        return searchService.search(request)
                .doOnNext(result -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                        String uiResponse = mapper.writerWithDefaultPrettyPrinter()
                                .writeValueAsString(result);

                        // Write to file for debugging
                        java.nio.file.Files.writeString(
                            java.nio.file.Paths.get("ui-response-output.json"),
                            uiResponse,
                            java.nio.charset.StandardCharsets.UTF_8
                        );
                        	
                        System.out.println("====== Final Response to UI ======");
                        System.out.println("Response written to: ui-response-output.json");
                        System.out.println("Onward flights: " + (result.getOnward() != null ? result.getOnward().size() : 0));
                        System.out.println("Return flights: " + (result.getReturns() != null ? result.getReturns().size() : 0));
                        System.out.println("Response size: " + uiResponse.length() + " characters");
                        System.out.println("==================================");
                        System.out.println("====================="+uiResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        
    }
}
