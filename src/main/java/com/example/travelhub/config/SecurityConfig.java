package com.example.travelhub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.Customizer;

public class SecurityConfig {
	
	  @Bean
	    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
	        return http
	                .csrf(ServerHttpSecurity.CsrfSpec::disable)
	                .cors(Customizer.withDefaults())
	                .authorizeExchange(exchanges -> exchanges
	                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	                        .pathMatchers("/api/**").permitAll()
	                        .anyExchange().authenticated()
	                )
	                .build();
	    }

}
