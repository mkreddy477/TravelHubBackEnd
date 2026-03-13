package com.example.travelhub.hotelbooking.service;

import com.example.travelhub.hotelbooking.models.CancellationPolicyResponse;
import com.example.travelhub.hotelbooking.models.CancellationPolicyRequest;
import reactor.core.publisher.Mono;

public interface HotelCancellationPolicyService {
    Mono<CancellationPolicyResponse> getCancellationPolicy(CancellationPolicyRequest request);
}