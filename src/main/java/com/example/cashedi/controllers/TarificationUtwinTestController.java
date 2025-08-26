package com.example.cashedi.controllers;

import com.example.cashedi.models.TarificationRequestUtwin;
import com.example.cashedi.models.TarificationResponseUtwin;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/test")
public class TarificationUtwinTestController {

    private static final Logger logger = LoggerFactory.getLogger(TarificationUtwinTestController.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/utwin-deserialize")
    public ResponseEntity<String> testDeserialization(@RequestBody String rawJson) {
        logger.info("🔍 TEST - Raw JSON reçu: {}", rawJson);
        
        try {
            // Test de désérialisation manuelle
            TarificationRequestUtwin request = objectMapper.readValue(rawJson, TarificationRequestUtwin.class);
            
            logger.info("🔍 TEST - Désérialisation réussie!");
            logger.info("🔍 TEST - Objet request: {}", request);
            logger.info("🔍 TEST - Souscripteur: {}", request.getSouscripteur());
            logger.info("🔍 TEST - Besoin: {}", request.getBesoin());
            logger.info("🔍 TEST - Assures: {}", request.getAssures());
            
            if (request.getAssures() != null) {
                for (int i = 0; i < request.getAssures().size(); i++) {
                    var assure = request.getAssures().get(i);
                    logger.info("🔍 TEST - Assuré[{}]: codeRegime={}, codeTypeRole={}, dateNaissance={}", 
                        i, assure.getCodeRegimeObligatoire(), assure.getCodeTypeRole(), assure.getDateDeNaissance());
                }
            }
            
            return ResponseEntity.ok("Désérialisation réussie! Vérifiez les logs.");
            
        } catch (Exception e) {
            logger.error("❌ TEST - Erreur désérialisation: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Erreur désérialisation: " + e.getMessage());
        }
    }
}
