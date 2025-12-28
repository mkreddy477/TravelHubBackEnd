package com.example.travelhub.flightbooking.repository;

import com.example.travelhub.flightbooking.entity.UserDetails;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * R2DBC Repository for USER_DETAILS table
 */
@Repository
public interface UserDetailsRepository extends R2dbcRepository<UserDetails, Long> {
    
    Mono<UserDetails> findByUsername(String username);
    
    Mono<UserDetails> findByPrimaryEmail(String primaryEmail);
}
