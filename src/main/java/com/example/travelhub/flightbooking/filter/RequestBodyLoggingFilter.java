package com.example.travelhub.flightbooking.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
public class RequestBodyLoggingFilter  implements WebFilter {
private static final Logger log = LoggerFactory.getLogger(RequestBodyLoggingFilter.class);
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.getRequest().getURI().getPath().contains("/hold")) {
            log.info("=== HOLD REQUEST RECEIVED ===");
            log.info("Method: {}", exchange.getRequest().getMethod());
            log.info("URI: {}", exchange.getRequest().getURI());
            log.info("Headers: {}", exchange.getRequest().getHeaders());
        }
        return chain.filter(exchange);
    }
}


