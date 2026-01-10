package com.example.travelhub.hotelbooking.controller;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelhub.hotelbooking.models.HotelSearchRequest;
import com.example.travelhub.hotelbooking.models.HotelSearchResponse;
import com.example.travelhub.hotelbooking.service.HotelSearchService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/hotels")
public class HotelSearchController {

    private final HotelSearchService searchService;
    private final AtomicLong searchHitCount = new AtomicLong(0);
    private final boolean dumpSearchResponse;

    public HotelSearchController(
            HotelSearchService searchService,
            @Value("${travelhub.debug.dump-search-response:false}") boolean dumpSearchResponse) {
        this.searchService = searchService;
        this.dumpSearchResponse = dumpSearchResponse;
    }

    @PostMapping("/search")
    public Mono<HotelSearchResponse> search(@RequestBody HotelSearchRequest request, ServerHttpResponse response) throws JsonProcessingException {
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
                    if (!dumpSearchResponse) {
                        return;
                    }
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                        String uiResponse = mapper.writerWithDefaultPrettyPrinter()
                                .writeValueAsString(result);

                        // Write to file for debugging
                        java.nio.file.Files.writeString(
                            java.nio.file.Paths.get("hotel-ui-response-output.json"),
                            uiResponse,
                            java.nio.charset.StandardCharsets.UTF_8
                        );
                        
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .doOnTerminate(() -> {
                    long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
                    durationMs.set(ms);
                    System.out.println("/api/hotels/search completed in " + ms + " ms");
                });
    }
}