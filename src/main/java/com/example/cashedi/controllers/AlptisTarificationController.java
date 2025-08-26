package com.example.cashedi.controllers;

import com.example.cashedi.models.alptis.AlptisTarificationRequest;
import com.example.cashedi.models.alptis.AlptisTarificationResponseComplete;
import com.example.cashedi.services.AlptisTarificationService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/tarification")
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"}, allowCredentials = "true")
public class AlptisTarificationController {

    private static final Logger logger = LoggerFactory.getLogger(AlptisTarificationController.class);

    private final AlptisTarificationService tarificationService;

    public AlptisTarificationController(AlptisTarificationService tarificationService) {
        this.tarificationService = tarificationService;
    }

    @PostMapping("/alptis")
    public Mono<ResponseEntity<AlptisTarificationResponseComplete>> getAlptisTarif(@Valid @RequestBody AlptisTarificationRequest request) {
        logger.info("Received request for ALPTIS tarification: {}", request);

        return tarificationService.getTarification(request)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .onErrorResume(e -> {
                    logger.error("Error processing ALPTIS tarification request: {}", e.getMessage(), e);
                    return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    @GetMapping("/alptis/offres")
    public Mono<ResponseEntity<AlptisTarificationResponseComplete>> getOffres() {
        logger.info("Received request for ALPTIS offers list");

        return tarificationService.getOffres()
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .onErrorResume(e -> {
                    logger.error("Error fetching ALPTIS offers: {}", e.getMessage(), e);
                    return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }
}
