package com.example.cashedi.controllers;

import com.example.cashedi.models.TarificationRequestUtwin;
import com.example.cashedi.models.TarificationResponseUtwin;
import com.example.cashedi.services.TarificationServiceUtwin;
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
    public Mono<ResponseEntity<TarificationResponseUtwin>> getTarif(@RequestBody String rawJson) {
        logger.info("🔍 DEBUG - Raw JSON reçu: {}", rawJson);
        
        try {
            // Désérialisation manuelle comme dans le test qui fonctionne
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            TarificationRequestUtwin request = objectMapper.readValue(rawJson, TarificationRequestUtwin.class);
            
            logger.info("🔍 DEBUG - Désérialisation réussie: {}", request);
            logger.info("🔍 DEBUG - Souscripteur: {}", request.getSouscripteur());
            logger.info("🔍 DEBUG - Besoin: {}", request.getBesoin());
            logger.info("🔍 DEBUG - Assures: {}", request.getAssures());
            
            if (request.getAssures() != null) {
                for (int i = 0; i < request.getAssures().size(); i++) {
                    logger.info("🔍 DEBUG - Assuré[{}]: {}", i, request.getAssures().get(i));
                }
            }

            // Validation supplémentaire pour l'assuré principal
            if (!request.hasAssurePrincipal()) {
                logger.error("Aucun assuré principal trouvé dans la requête");
                TarificationResponseUtwin errorResponse = new TarificationResponseUtwin();
                return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST));
            }

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
                
        } catch (Exception e) {
            logger.error("❌ Erreur désérialisation JSON: {}", e.getMessage(), e);
            return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
    }
}