package com.example.cashedi.controllers;

import com.example.cashedi.services.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/Sante/v1/Documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Récupère un document par son identifiant et le retourne en Base64.
     *
     * @param identifiant L'identifiant technique du document.
     * @return Le contenu du document en Base64 ou une erreur si non trouvé.
     */
    @GetMapping("/{identifiant}")
    public ResponseEntity<?> getDocument(@PathVariable String identifiant) {
        Optional<String> base64ContentOpt = documentService.getDocumentContentById(identifiant);

        if (base64ContentOpt.isPresent()) {
            return ResponseEntity.ok(base64ContentOpt.get());
        } else {
            Map<String, Object> errorResponse = Map.of(
                    "status", "error",
                    "timestamp", System.currentTimeMillis(),
                    "message", "Document non trouvé pour l'identifiant: " + identifiant,
                    "code", HttpStatus.NOT_FOUND.value()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
