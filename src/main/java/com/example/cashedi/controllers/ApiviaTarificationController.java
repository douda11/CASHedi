package com.example.cashedi.controllers;

import com.example.cashedi.models.apivia.ApiviaTarificationRequest;
import com.example.cashedi.models.apivia.ApiviaTarificationResponse;
import com.example.cashedi.services.ApiviaTarificationService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/tarification")
public class ApiviaTarificationController {

    private static final Logger logger = LoggerFactory.getLogger(ApiviaTarificationController.class);

    private final ApiviaTarificationService tarificationService;

    public ApiviaTarificationController(ApiviaTarificationService tarificationService) {
        this.tarificationService = tarificationService;
    }

    @PostMapping("/apivia")
    public Mono<ResponseEntity<ApiviaTarificationResponse>> getApiviaTarif(@Valid @RequestBody ApiviaTarificationRequest request) {
        logger.info("Received request for APIVIA tarification: {}", request);

        return tarificationService.getTarification(request)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .onErrorResume(e -> {
                    logger.error("Error processing APIVIA tarification request: {}", e.getMessage(), e);
                    return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    @GetMapping("/produits")
    public Mono<ApiviaTarificationResponse> getProduits() {
        return tarificationService.getProduits();
    }
}
