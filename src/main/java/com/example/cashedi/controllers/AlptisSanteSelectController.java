package com.example.cashedi.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/api/alptis/sante-select")
public class AlptisSanteSelectController {

    private static final Logger logger = LoggerFactory.getLogger(AlptisSanteSelectController.class);

    private final RestTemplate restTemplate;

    @Value("${alptis.api.sante-select.base-url}")
    private String alptisApiBaseUrl;

    @Value("${alptis.api.key}")
    private String alptisApiKey;

    public AlptisSanteSelectController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private ResponseEntity<String> forwardRequest(String path, Map<String, Object> requestBody) {
        logger.info("Forwarding request to Alptis Santé Select API at path: {}", path);
        logger.debug("Request body: {}", requestBody);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Alptis-Api-Key", alptisApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        String url = UriComponentsBuilder.fromUriString(alptisApiBaseUrl)
                .path(path)
                .toUriString();

        try {
            return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException e) {
            logger.error("Error from Alptis API: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            // Re-throw the exception to be handled by the global exception handler
            throw e;
        }
    }

    @PostMapping("/tarification/tarifer-niveaux-entiers")
    public ResponseEntity<String> tariferNiveauxEntiers(@RequestBody Map<String, Object> requestBody) {
        return forwardRequest("/tarification/tarifer-niveaux-entiers", requestBody);
    }

    @PostMapping("/tarification/tarifer-modulaire")
    public ResponseEntity<String> tariferModulaire(@RequestBody Map<String, Object> requestBody) {
        return forwardRequest("/tarification/tarifer-modulaire", requestBody);
    }

    @PostMapping("/creer-projet")
    public ResponseEntity<String> creerProjet(@RequestBody Map<String, Object> requestBody) {
        return forwardRequest("/creer-projet", requestBody);
    }
}
