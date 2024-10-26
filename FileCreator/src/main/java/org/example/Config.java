package org.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Config {

    @Value("${file_creator.api.key}")
    private String apiKey;

    @Value("${file_creator.database_url}")
    private String databaseUrl;

    @Value("${file_creator.api.header}")
    private String apiHeader;

    @Bean
    public ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public WebClient dbWebClient() {
        return WebClient
                .builder()
                .baseUrl(databaseUrl)
                .defaultHeader(apiHeader, apiKey)
                .build();
    }
}
