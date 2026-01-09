package com.example.travelhub.hotelbooking.controller;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelhub.hotelbooking.models.HotelDetailResponse;
import com.example.travelhub.hotelbooking.models.HotelOperationRequest;
import com.example.travelhub.hotelbooking.service.HotelDetailService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/hotels")
public class HotelDetailController {

    private final HotelDetailService detailService;
    private final AtomicLong detailHitCount = new AtomicLong(0);
    private final boolean dumpDetailResponse;

    public HotelDetailController(
            HotelDetailService detailService,
            @Value("${travelhub.debug.dump-detail-response:false}") boolean dumpDetailResponse) {
        this.detailService = detailService;
        this.dumpDetailResponse = dumpDetailResponse;
    }

    @PostMapping("/detail")
    public Mono<HotelDetailResponse> detail(@RequestBody HotelOperationRequest request, ServerHttpResponse response) throws JsonProcessingException {
        long hitNo = detailHitCount.incrementAndGet();
        System.out.println("Incoming hotel detail request (hit #" + hitNo + "): Hotel ID: " + request.getId());

        final long startNanos = System.nanoTime();
        AtomicLong durationMs = new AtomicLong(-1);

        response.beforeCommit(() -> {
            long ms = durationMs.get();
            if (ms >= 0) {
                response.getHeaders().set("X-Response-Time-Ms", String.valueOf(ms));
            }
            response.getHeaders().set("X-Detail-Hit-Count", String.valueOf(hitNo));
            return Mono.empty();
        });

        return detailService.getDetails(request)
                .doOnNext(result -> {
                    if (!dumpDetailResponse) {
                        return;
                    }
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                        String uiResponse = mapper.writerWithDefaultPrettyPrinter()
                                .writeValueAsString(result);

                        // Write to file for debugging
                        java.nio.file.Files.writeString(
                            java.nio.file.Paths.get("hotel-detail-ui-response-output.json"),
                            uiResponse,
                            java.nio.charset.StandardCharsets.UTF_8
                        );
                        
                        System.out.println("====== Final Detail Response to UI ======");
                        System.out.println("Response written to: hotel-detail-ui-response-output.json");
                        System.out.println("Hotel ID: " + (result.getHotel() != null ? result.getHotel().getId() : "N/A"));
                        System.out.println("Hotel Name: " + (result.getHotel() != null ? result.getHotel().getName() : "N/A"));
                        System.out.println("Options available: " + (result.getHotel() != null && result.getHotel().getOps() != null 
                                ? result.getHotel().getOps().size() : 0));
                        System.out.println("Response size: " + uiResponse.length() + " characters");
                        System.out.println("==========================================");
                        System.out.println("=====================" + uiResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .doOnTerminate(() -> {
                    long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
                    durationMs.set(ms);
                    System.out.println("/api/hotels/detail completed in " + ms + " ms");
                });
    }
}