package com.example.travelhub.hotelbooking.service;

import com.example.travelhub.hotelbooking.models.CancellationPolicyResponse;
import com.example.travelhub.hotelbooking.models.HotelOperationRequest;
import reactor.core.publisher.Mono;

public interface HotelCancellationPolicyService {
    Mono<CancellationPolicyResponse> getCancellationPolicy(HotelOperationRequest request);
}