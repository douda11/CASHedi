package com.example.cashedi.controllers;

import com.example.cashedi.models.AcheelTarificationRequest;
import com.example.cashedi.models.AcheelTarificationResponse;
import com.example.cashedi.services.DeuxMaTarificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/deuxma/v1/tarifs")
public class DeuxMaTarificationController {

    private static final Logger logger = LoggerFactory.getLogger(DeuxMaTarificationController.class);

    private final DeuxMaTarificationService tarificationService;

    public DeuxMaTarificationController(DeuxMaTarificationService tarificationService) {
        this.tarificationService = tarificationService;
    }

    @PostMapping("/acheel")
    public Mono<ResponseEntity<AcheelTarificationResponse>> getAcheelTarif(@Valid @RequestBody AcheelTarificationRequest request) {
        logger.info("Requête reçue pour les tarifs ACHEEL de 2MA: {}", request);

        return tarificationService.getAcheelTarif(request)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .onErrorResume(e -> {
                    logger.error("Erreur lors du traitement de la requête de tarifs ACHEEL: {}", e.getMessage(), e);
                    return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }
}
