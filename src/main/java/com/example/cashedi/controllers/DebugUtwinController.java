package com.example.cashedi.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/debug")
public class DebugUtwinController {

    private static final Logger logger = LoggerFactory.getLogger(DebugUtwinController.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/utwin-raw")
    public ResponseEntity<String> debugUtwinRequest(@RequestBody String rawJson) {
        logger.info("🔍 Raw JSON reçu: {}", rawJson);
        
        try {
            JsonNode jsonNode = objectMapper.readTree(rawJson);
            logger.info("🔍 JSON parsé avec succès: {}", jsonNode.toPrettyString());
            
            // Vérifier les champs spécifiques
            JsonNode souscripteur = jsonNode.get("souscripteur");
            JsonNode besoin = jsonNode.get("besoin");
            JsonNode assures = jsonNode.get("assures");
            
            logger.info("🔍 Souscripteur: {}", souscripteur);
            logger.info("🔍 Besoin: {}", besoin);
            logger.info("🔍 Assures: {}", assures);
            
            if (assures != null && assures.isArray()) {
                for (int i = 0; i < assures.size(); i++) {
                    JsonNode assure = assures.get(i);
                    logger.info("🔍 Assuré[{}]: {}", i, assure);
                }
            }
            
            return ResponseEntity.ok("JSON reçu et parsé avec succès. Vérifiez les logs.");
            
        } catch (Exception e) {
            logger.error("❌ Erreur lors du parsing JSON: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Erreur parsing JSON: " + e.getMessage());
        }
    }
}
