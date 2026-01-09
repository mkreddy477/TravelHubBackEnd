package com.example.travelhub.hotelbooking.controller;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelhub.hotelbooking.models.CancellationPolicyResponse;
import com.example.travelhub.hotelbooking.models.HotelOperationRequest;
import com.example.travelhub.hotelbooking.service.HotelCancellationPolicyService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/hotels")
public class HotelCancellationPolicyController {

    private final HotelCancellationPolicyService cancellationPolicyService;
    private final AtomicLong cancellationPolicyHitCount = new AtomicLong(0);
    private final boolean dumpCancellationPolicyResponse;

    public HotelCancellationPolicyController(
            HotelCancellationPolicyService cancellationPolicyService,  // Inject interface
            @Value("${travelhub.debug.dump-cancellation-policy-response:false}") boolean dumpCancellationPolicyResponse) {
        this.cancellationPolicyService = cancellationPolicyService;
        this.dumpCancellationPolicyResponse = dumpCancellationPolicyResponse;
    }

    @PostMapping("/cancellation-policy")
    public Mono<CancellationPolicyResponse> getCancellationPolicy(
            @RequestBody HotelOperationRequest request, 
            ServerHttpResponse response) throws JsonProcessingException {
        
        long hitNo = cancellationPolicyHitCount.incrementAndGet();
        System.out.println("Incoming cancellation policy request (hit #" + hitNo + 
                "): Hotel ID: " + request.getId() + ", Option ID: " + request.getOptionId());

        final long startNanos = System.nanoTime();
        AtomicLong durationMs = new AtomicLong(-1);

        response.beforeCommit(() -> {
            long ms = durationMs.get();
            if (ms >= 0) {
                response.getHeaders().set("X-Response-Time-Ms", String.valueOf(ms));
            }
            response.getHeaders().set("X-Cancellation-Policy-Hit-Count", String.valueOf(hitNo));
            return Mono.empty();
        });

        return cancellationPolicyService.getCancellationPolicy(request)
                .doOnNext(result -> {
                    if (!dumpCancellationPolicyResponse) {
                        return;
                    }
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

                        String uiResponse = mapper.writerWithDefaultPrettyPrinter()
                                .writeValueAsString(result);

                        java.nio.file.Files.writeString(
                            java.nio.file.Paths.get("hotel-cancellation-policy-ui-response-output.json"),
                            uiResponse,
                            java.nio.charset.StandardCharsets.UTF_8
                        );
                        
                        System.out.println("====== Final Cancellation Policy Response to UI ======");
                        System.out.println("Response written to: hotel-cancellation-policy-ui-response-output.json");
                        System.out.println("Hotel ID: " + result.getId());
                        System.out.println("Policy ID: " + (result.getCancellationPolicy() != null 
                                ? result.getCancellationPolicy().getId() : "N/A"));
                        System.out.println("Is Refundable: " + (result.getCancellationPolicy() != null 
                                ? result.getCancellationPolicy().isIfr() : "N/A"));
                        System.out.println("No Refund: " + (result.getCancellationPolicy() != null 
                                ? result.getCancellationPolicy().isInr() : "N/A"));
                        System.out.println("Penalty Details: " + (result.getCancellationPolicy() != null 
                                && result.getCancellationPolicy().getPd() != null 
                                ? result.getCancellationPolicy().getPd().size() : 0));
                        System.out.println("Timezone: GMT+5:30 (Kolkata)");
                        System.out.println("Response size: " + uiResponse.length() + " characters");
                        System.out.println("======================================================");
                        System.out.println("=====================" + uiResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .doOnTerminate(() -> {
                    long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);
                    durationMs.set(ms);
                    System.out.println("/api/hotels/cancellation-policy completed in " + ms + " ms");
                });
    }
}