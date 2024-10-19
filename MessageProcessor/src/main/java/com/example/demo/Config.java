package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Config {

    @Bean
    public WebClient dbWebClient() {
        return WebClient
                .builder()
                .baseUrl("http://localhost:8082")
                .build();
    }
}
