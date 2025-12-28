package com.example.travelhub.auth.repository;

import com.example.travelhub.auth.entity.UserDetails;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AuthUserDetailsRepository extends R2dbcRepository<UserDetails, Long> {

    Mono<UserDetails> findByUsername(String username);

    Mono<UserDetails> findByPrimaryEmail(String primaryEmail);

    Mono<Boolean> existsByUsername(String username);

    Mono<Boolean> existsByPrimaryEmail(String primaryEmail);
}
