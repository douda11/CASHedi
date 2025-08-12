package com.example.cashedi.controllers;

import com.example.cashedi.services.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

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
            return ResponseEntity.ok(Map.of("base64", base64ContentOpt.get()));
        } else {
            Map<String, String> errorResponse = Map.of("error", "Document non trouvé", "message", "Le document avec l'identifiant " + identifiant + " n'a pas été trouvé.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
