package com.example.cashedi.services;

import com.example.cashedi.models.TokenResponse;
import org.springframework.http.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class DeuxMaAuthService {

    private static final Logger logger = LoggerFactory.getLogger(DeuxMaAuthService.class);

    private final WebClient webClient;

    @Value("${deuxma.api.url:https://dev.ws.sante.2ma.fr}")
    private String apiUrl;

    @Value("${deuxma.api.user:BROKINS}")
    private String apiUser;

    @Value("${deuxma.api.password:7w9jG5MS$WO*}")
    private String apiPassword;

    private String cachedToken;

    public DeuxMaAuthService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<String> authenticate() {
        if (cachedToken != null) {
            logger.info("Using cached 2MA token.");
            return Mono.just(cachedToken);
        }

        String authUrl = apiUrl + "/default/auth/";
        logger.info("Attempting to authenticate with 2MA at URL: {}", authUrl);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("user", apiUser);
        formData.add("password", apiPassword);
        logger.info("Using credentials for user: {}", apiUser);

        return webClient.post()
                .uri(authUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .doOnSuccess(response -> {
                    this.cachedToken = response.getToken();
                    logger.info("Successfully authenticated with 2MA and received token.");
                })
                .doOnError(error -> {
                    logger.error("Failed to authenticate with 2MA. Status: {}, Response: {}", 
                        error instanceof org.springframework.web.reactive.function.client.WebClientResponseException ? ((org.springframework.web.reactive.function.client.WebClientResponseException) error).getStatusCode() : "N/A",
                        error instanceof org.springframework.web.reactive.function.client.WebClientResponseException ? ((org.springframework.web.reactive.function.client.WebClientResponseException) error).getResponseBodyAsString() : "N/A");
                    logger.error("Full error details: ", error);
                })
                .map(TokenResponse::getToken);
    }
}
