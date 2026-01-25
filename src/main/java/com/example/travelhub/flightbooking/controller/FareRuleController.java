package com.example.travelhub.flightbooking.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelhub.flightbooking.models.farerulemodels.FareRuleRequest;
import com.example.travelhub.flightbooking.service.FareRuleService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping({"/api/v1/flights", "/api/flights"})
public class FareRuleController {

    private final FareRuleService fareRuleService;
    private final ObjectMapper objectMapper;

    public FareRuleController(FareRuleService fareRuleService, ObjectMapper objectMapper) {
        super();
        this.fareRuleService = fareRuleService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(
            value = "/farerule",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<JsonNode>> getFareRule(@Valid @RequestBody FareRuleRequest request) {
      
    	System.out.println("fareRuleData");
    	
    	return fareRuleService
                .getFareRule(request)
                .map(body -> {
                    ObjectNode wrapper = objectMapper.createObjectNode();
                    wrapper.set("fareRule", body);
                    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(wrapper);
                })
                .defaultIfEmpty(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.createObjectNode().putNull("fareRule")));
    }
}
