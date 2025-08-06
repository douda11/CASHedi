package com.example.cashedi.Config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApiConfig {

    @Value("${utwin.api.base-url}")
    private String baseUrl;

    @Value("${utwin.api.licence}")
    private String licenceKey;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getLicenceKey() {
        return licenceKey;
    }
}
