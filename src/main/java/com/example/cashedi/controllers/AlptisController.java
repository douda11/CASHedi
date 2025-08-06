package com.example.cashedi.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/alptis/sante-pro-plus")
public class AlptisController {

    private final RestTemplate restTemplate;

    @Value("${alptis.api.base-url}")
    private String alptisApiBaseUrl;

    @Value("${alptis.api.key}")
    private String alptisApiKey;

    public AlptisController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/tarification/obtenir-tarifs")
    public ResponseEntity<String> getTarifs(@RequestBody String requestBody) {
        // Préparer les headers pour l'API Alptis
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Alptis-Api-Key", alptisApiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Construire l'URL de la cible
        String url = UriComponentsBuilder.fromUriString(alptisApiBaseUrl)
                .path("/tarification/obtenir-tarifs")
                .toUriString();

        // Transférer la requête et retourner la réponse
        return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}
