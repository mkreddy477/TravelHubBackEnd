package com.example.travelhub.flightbooking.service;

import com.example.travelhub.flightbooking.models.farerulemodels.FareRuleRequest;
import com.fasterxml.jackson.databind.JsonNode;

import reactor.core.publisher.Mono;

public interface FareRuleService {

    Mono<JsonNode> getFareRule(FareRuleRequest request);
}
