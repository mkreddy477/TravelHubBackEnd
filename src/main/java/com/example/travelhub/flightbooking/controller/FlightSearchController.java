package com.example.travelhub.flightbooking.controller;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
    private final AtomicLong searchHitCount = new AtomicLong(0);
    private final boolean dumpSearchResponse;

    public FlightSearchController(
            TripjackFlightSearchService searchService,
            @Value("${travelhub.debug.dump-search-response:false}") boolean dumpSearchResponse) {
        this.searchService = searchService;
        this.dumpSearchResponse = dumpSearchResponse;
    }

    @PostMapping("/search")
    public Mono<FlightSearchResultGroup> search(@RequestBody FlightSearchRequest request, ServerHttpResponse response) throws JsonProcessingException {
        long hitNo = searchHitCount.incrementAndGet();
        System.out.println("Incoming UI request (hit #" + hitNo + "): " + request);

        final long startNanos = System.nanoTime();
        AtomicLong durationMs = new AtomicLong(-1);

        response.beforeCommit(() -> {
            long ms = durationMs.get();
            if (ms >= 0) {
                response.getHeaders().set("X-Response-Time-Ms", String.valueOf(ms));
            }
            response.getHeaders().set("X-Search-Hit-Count", String.valueOf(hitNo));
            return Mono.empty();
        });

        return searchService.search(request)
                .doOnNext(result -> {
                    // Always print response summary
                    System.out.println("====== RESPONSE TO FRONTEND ======");
                    System.out.println("Onward flights count: " + (result.getOnward() != null ? result.getOnward().size() : 0));
                    System.out.println("Return flights count: " + (result.getReturns() != null ? result.getReturns().size() : 0));
                    System.out.println("Combos count: " + (result.getCombos() != null ? result.getCombos().size() : 0));
                    
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                        String uiResponse = mapper.writerWithDefaultPrettyPrinter()
                                .writeValueAsString(result);

                        System.out.println("Full JSON response:");
                        System.out.println(uiResponse);
                        
                        // Write to file for debugging
                        java.nio.file.Files.writeString(
                            java.nio.file.Paths.get("ui-response-output.json"),
                            uiResponse,
                            java.nio.charset.StandardCharsets.UTF_8
                        );
                        System.out.println("Response also written to: ui-response-output.json");
                        System.out.println("==================================");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .doOnTerminate(() -> {
                    long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
                    durationMs.set(ms);
                    System.out.println("/api/flights/search completed in " + ms + " ms");
                });
    }
}
