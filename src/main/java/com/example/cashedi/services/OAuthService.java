package com.example.cashedi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects; // Import for Objects.requireNonNull

@Service
public class OAuthService {
    private static final Logger logger = LoggerFactory.getLogger(OAuthService.class);

    @Value("${april.oauth.token-url}")
    private String tokenUrl;

    @Value("${april.oauth.client-id}")
    private String clientId;

    @Value("${april.oauth.client-secret}")
    private String clientSecret;

    // Add a cache to store the token to avoid repeated requests
    private final Map<String, String> tokenCache = new ConcurrentHashMap<>();
    private static final String CACHE_KEY = "april_oauth_token";


    private final RestTemplate restTemplate;

    public OAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getOAuthToken() {
        // Try to retrieve the token from the cache
        String cachedToken = tokenCache.get(CACHE_KEY);
        if (cachedToken != null) {
            logger.info("OAuth token retrieved from cache.");
            return cachedToken;
        }

        logger.info("Attempting to retrieve OAuth2 token...");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        // Use values injected by @Value, not hardcoded constants
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        // *** CRITICAL CORRECTION: Removed the "scope" parameter. ***
        // The API's token endpoint for client_credentials grant type does not expect this parameter,
        // as indicated by the "invalid_scope" error. Permissions are likely determined by the client_id/client_secret.

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            // Use the value injected by @Value for the token URL
            ResponseEntity<Map> response = restTemplate.exchange(
                    tokenUrl, // Use injected tokenUrl
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String token = (String) Objects.requireNonNull(response.getBody()).get("access_token");
                if (token == null) {
                    throw new RuntimeException("Access token not found in the OAuth response.");
                }
                tokenCache.put(CACHE_KEY, token); // Store the token in the cache
                logger.info("Token retrieved successfully.");
                return token;
            } else {
                logger.error("Error retrieving token: HTTP Code = {}, Response Body = {}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("Error retrieving OAuth token. HTTP Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error retrieving token: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve OAuth token.", e);
        }
    }
}
