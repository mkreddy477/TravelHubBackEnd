package com.example.travelhub.flightbooking.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

//    @Bean
//    public WebClient tripjackWebClient(@Value("${tripjack.api.base-url}") String baseUrl,
//                                       WebClient.Builder builder) {
//        return builder
//                .baseUrl(baseUrl)          // e.g. https://apitest.tripjack.com
//                .build();
//    }
	
	
	@Bean
	public WebClient tripjackWebClient() {
	    ExchangeStrategies strategies = ExchangeStrategies.builder()
	        .codecs(configurer -> configurer
	            .defaultCodecs()
	            .maxInMemorySize(10 * 1024 * 1024)) // 10 MB
	        .build();
	    
	    return WebClient.builder()
	        .baseUrl("https://apitest.tripjack.com")
	        .exchangeStrategies(strategies)
	        .build();
	}
}