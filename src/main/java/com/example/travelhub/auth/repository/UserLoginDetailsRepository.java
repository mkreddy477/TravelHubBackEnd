package com.example.travelhub.auth.repository;

import com.example.travelhub.auth.entity.UserLoginDetails;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserLoginDetailsRepository extends R2dbcRepository<UserLoginDetails, Long> {

    Mono<UserLoginDetails> findByUserDetailId(Long userDetailId);
}
