package com.example.cashedi.controllers;

import com.example.cashedi.models.apivia.devis.GeneratePdfDevisRequest;
import com.example.cashedi.models.apivia.devis.GeneratePdfDevisResponse;
import com.example.cashedi.services.ApiviaTarificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.cashedi.models.apivia.devis.DevisList;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import java.util.Map;

@RestController
@RequestMapping("/api/apivia")
public class ApiviaQuoteController {

    private static final Logger logger = LoggerFactory.getLogger(ApiviaQuoteController.class);

    private final ApiviaTarificationService apiviaTarificationService;

    @Autowired
    public ApiviaQuoteController(ApiviaTarificationService apiviaTarificationService) {
        this.apiviaTarificationService = apiviaTarificationService;
    }

    @PostMapping("/generate-devis")
    public Mono<ResponseEntity<?>> generateDevis(@RequestBody GeneratePdfDevisRequest request,
                                                     @RequestParam(defaultValue = "false") boolean test) {
        logger.info("Received generateDevis request: {}", request);
        Mono<GeneratePdfDevisResponse> responseMono = test
                ? apiviaTarificationService.testGeneratePdfDevis(request)
                : apiviaTarificationService.generatePdfDevis(request);

        return responseMono.map(response -> {
            Object listObject = response.getList();

            if (listObject instanceof String) {
                // Handle API error message returned as a string in the 'list' field
                return ResponseEntity.badRequest().body(Map.of("status", "error", "message", listObject));
            }

            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            if (listObject instanceof Map) {
                // Convert the map to a single DevisList object
                DevisList devis = mapper.convertValue(listObject, DevisList.class);

                if (devis.getFiles() != null && !devis.getFiles().isEmpty()) {
                    return handlePdfDownload(List.of(devis)); // handlePdfDownload expects a List
                }
            } else if (listObject instanceof List) {
                // This handles the case where it might still be a list, for robustness
                List<DevisList> devisList = mapper.convertValue(listObject, new com.fasterxml.jackson.core.type.TypeReference<List<DevisList>>() {});

                if (!devisList.isEmpty() && devisList.get(0).getFiles() != null && !devisList.get(0).getFiles().isEmpty()) {
                    return handlePdfDownload(devisList);
                }
            }

            // Fallback for other cases or empty lists
            return ResponseEntity.ok(response);
        });
    }

    private ResponseEntity<?> handlePdfDownload(List<DevisList> devisList) {
        try {
            String base64Pdf = devisList.get(0).getFiles().get(0).getFile();
            byte[] pdfBytes = java.util.Base64.getDecoder().decode(base64Pdf);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "devis.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Erreur lors du traitement du fichier PDF: " + e.getMessage()));
        }
    }
}