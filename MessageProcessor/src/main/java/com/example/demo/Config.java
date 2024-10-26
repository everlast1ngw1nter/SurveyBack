package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Config {

    @Value("${message_processor.api.key}")
    private String apiKey;

    @Value("${message_processor.api.header}")
    private String apiHeader;

    @Value("${message_processor.database_url}")
    private String databaseUrl;

    @Bean
    public WebClient dbWebClient() {
        return WebClient
                .builder()
                .baseUrl(databaseUrl)
                .defaultHeader(apiHeader, apiKey)
                .build();
    }
}
