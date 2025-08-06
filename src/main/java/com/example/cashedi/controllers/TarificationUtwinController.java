package com.example.cashedi.controllers;

import com.example.cashedi.models.Assure;
import com.example.cashedi.models.TarificationRequestUtwin;
import com.example.cashedi.models.TarificationResponseUtwin;
import com.example.cashedi.services.TarificationServiceUtwin;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/Sante/v1/Tarifs")
public class TarificationUtwinController {

    private static final Logger logger = LoggerFactory.getLogger(TarificationUtwinController.class);

    private final TarificationServiceUtwin service;

    public TarificationUtwinController(TarificationServiceUtwin service) {
        this.service = service;
    }

    @PostMapping
    public Mono<ResponseEntity<TarificationResponseUtwin>> getTarif(@Valid @RequestBody TarificationRequestUtwin request) {
        logger.info("Requête reçue pour Utwin tarifs: {}", request);

        return service.getTarifs(request)
                .map(response -> {
                    if (response.getMessages() != null && !response.getMessages().isEmpty()) {
                        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                    }
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .onErrorResume(e -> {
                    logger.error("Erreur lors du traitement de la requête Utwin tarifs: {}", e.getMessage(), e);
                    return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }}