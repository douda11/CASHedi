package com.example.cashedi.controllers;

import com.example.cashedi.services.AprilApiService;
import com.example.cashedi.services.DocumentService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/healthProtection/projects")
public class HealthProtectionController {

    private final AprilApiService aprilApiService;
    private final DocumentService documentService;

    public HealthProtectionController(AprilApiService aprilApiService, DocumentService documentService) {
        this.aprilApiService = aprilApiService;
        this.documentService = documentService;
    }

    /**
     * Endpoint pour calculer un tarif.
     *
     * @param pricingType  Type de tarification (obligatoire).
     * @param withSchedule Indique si l'échéancier doit être inclus (optionnel).
     * @param requestBody  Payload JSON contenant les détails du projet.
     * @return Réponse contenant le tarif calculé ou un message d'erreur.
     */
    @PostMapping("/prices")
    public ResponseEntity<?> calculateTarif(
            @RequestParam String pricingType,
            @RequestParam(required = false) Boolean withSchedule,
            @RequestBody Map<String, Object> requestBody) {

        try {
            // Validation des champs obligatoires
            validateRequest(requestBody);

            // Calculer le tarif (retourne désormais une List<Map>)
            List<Map<String, Object>> tarifResponse = aprilApiService.calculateTarif(
                    requestBody,
                    pricingType,
                    withSchedule
            );

            // Formatage de la réponse
            return buildSuccessResponse(tarifResponse);

        } catch (IllegalArgumentException e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors du calcul du tarif: " + e.getMessage()
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> createProject(
            @RequestParam("marketingParameters.requestType") String requestType,
            @RequestBody Map<String, Object> requestBody) {

        try {
            // Valider que le requestType est l'un des types attendus
            if (!"Quotation".equals(requestType) && !"ContactRequest".equals(requestType)) {
                return buildErrorResponse(HttpStatus.BAD_REQUEST, "La valeur de 'marketingParameters.requestType' est invalide.");
            }

            // Pour une demande de devis, la structure 'subscriber' doit être l'objet personne principal.
            if ("Quotation".equals(requestType)) {
                prepareSubscriber(requestBody);


            }

            // Appeler le service pour créer le projet (devis ou contact)
            Map<String, Object> projectResponse = aprilApiService.createProject(requestBody, requestType);

            // Retourner la réponse du service
            return ResponseEntity.ok(projectResponse);

        } catch (IllegalArgumentException e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la création du projet: " + e.getMessage()
            );
        }
    }

    @PostMapping("/commercial-proposal")
    public ResponseEntity<?> createCommercialProposal(
            @RequestParam String action,
            @RequestBody Map<String, Object> requestBody) {

        try {
            if (!"telechargement".equals(action) && !"envoiParEmail".equals(action)) {
                return buildErrorResponse(HttpStatus.BAD_REQUEST, "La valeur du paramètre 'action' est invalide.");
            }

            Map<String, Object> proposalResponse = aprilApiService.createCommercialProposal(requestBody, action);

            if ("telechargement".equals(action)) {
                // Extraire l'identifiant du document de la réponse complexe
                String documentId = extractUtwinDocumentId(proposalResponse);

                if (documentId == null) {
                    return buildErrorResponse(HttpStatus.NOT_FOUND, "Identifiant du document de devis introuvable dans la réponse.");
                }

                // Utiliser le DocumentService pour récupérer et retourner le PDF
                // NOTE: Ceci nécessitera de modifier DocumentService pour qu'il gère Utwin
                Optional<String> base64ContentOpt = documentService.getDocumentContentById(documentId);
                if (base64ContentOpt.isPresent()) {
                    byte[] pdfBytes = java.util.Base64.getDecoder().decode(base64ContentOpt.get());
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("attachment", "devis.pdf");
                    return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
                } else {
                    return buildErrorResponse(HttpStatus.NOT_FOUND, "Document non trouvé pour l'identifiant: " + documentId);
                }
            }

            return ResponseEntity.ok(proposalResponse);

        } catch (IllegalArgumentException e) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la création de la proposition commerciale: " + e.getMessage()
            );
        }
    }

    private String extractUtwinDocumentId(Map<String, Object> proposalResponse) {
        List<Map<String, Object>> propositions = (List<Map<String, Object>>) proposalResponse.get("propositions");
        if (propositions == null || propositions.isEmpty()) {
            return null;
        }

        List<Map<String, Object>> documents = (List<Map<String, Object>>) propositions.get(0).get("documents");
        if (documents == null || documents.isEmpty()) {
            return null;
        }

        for (Map<String, Object> doc : documents) {
            if ("PropositionCommercialeSante".equals(doc.get("type"))) {
                return (String) doc.get("identifiant");
            }
        }

        return null;
    }

    /**
     * Prépare l'objet 'subscriber' en y plaçant l'assuré principal.
     * L'API APRIL attend l'objet personne complet, pas seulement une référence ou un email.
     */
    @SuppressWarnings("unchecked")
    private void prepareSubscriber(Map<String, Object> requestBody) {
        if (requestBody == null || !requestBody.containsKey("persons") || !(requestBody.get("persons") instanceof List)) {
            throw new IllegalArgumentException("Le champ 'persons' est obligatoire et doit être une liste.");
        }

        List<Map<String, Object>> persons = (List<Map<String, Object>>) requestBody.get("persons");
        if (persons == null || persons.isEmpty()) {
            throw new IllegalArgumentException("La liste 'persons' ne peut être vide.");
        }

        // L'assuré principal est la première personne de la liste
        // L'assuré principal est la première personne de la liste
        Map<String, Object> mainInsuredPerson = persons.get(0);

        // L'API attend l'objet personne complet dans le champ 'subscriber', pas une référence.
        requestBody.put("subscriber", mainInsuredPerson);
    }

    /**
     * Valide la structure de la requête
     */
    private void validateRequest(Map<String, Object> requestBody) {
        if (requestBody == null) {
            throw new IllegalArgumentException("Le corps de la requête ne peut pas être null");
        }

        if (!requestBody.containsKey("products")) {
            throw new IllegalArgumentException("Le champ 'products' est obligatoire");
        }

        Object productsObj = requestBody.get("products");
        if (!(productsObj instanceof List)) {
            throw new IllegalArgumentException("Le champ 'products' doit être un tableau");
        }

        List<?> products = (List<?>) productsObj;
        if (products.isEmpty()) {
            throw new IllegalArgumentException("Au moins un produit doit être spécifié");
        }

        for (Object productObj : products) {
            if (!(productObj instanceof Map)) {
                throw new IllegalArgumentException("Chaque produit doit être un objet");
            }

            Map<?, ?> product = (Map<?, ?>) productObj;
            if (!product.containsKey("productCode")) {
                throw new IllegalArgumentException("Le champ 'productCode' est obligatoire pour chaque produit");
            }
        }
    }

    /**
     * Construit une réponse de succès standardisée
     */
    private ResponseEntity<Map<String, Object>> buildSuccessResponse(List<Map<String, Object>> data) {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "timestamp", System.currentTimeMillis(),
                "data", data,
                "count", data.size()
        ));
    }

    /**
     * Construit une réponse d'erreur standardisée
     */
    /**
     * Formate les champs de date dans le payload pour s'assurer qu'ils sont au format YYYY-MM-DD.
     */
    private void formatDatesInPayload(Map<String, Object> payload) {
        // Formater dateEffet
        if (payload.containsKey("besoin")) {
            Object besoinObj = payload.get("besoin");
            if (besoinObj instanceof Map) {
                Map<String, Object> besoinMap = (Map<String, Object>) besoinObj;
                if (besoinMap.containsKey("dateEffet")) {
                    String dateEffet = (String) besoinMap.get("dateEffet");
                    besoinMap.put("dateEffet", dateEffet.substring(0, 10));
                }
            }
        }

        // Formater les dates de naissance des assurés
        if (payload.containsKey("assures")) {
            Object assuresObj = payload.get("assures");
            if (assuresObj instanceof java.util.List) {
                java.util.List<Map<String, Object>> assuresList = (java.util.List<Map<String, Object>>) assuresObj;
                for (Map<String, Object> assure : assuresList) {
                    if (assure.containsKey("dateDeNaissance")) {
                        String dateNaissance = (String) assure.get("dateDeNaissance");
                        if (dateNaissance.length() > 10) {
                            assure.put("dateDeNaissance", dateNaissance.substring(0, 10));
                        }
                    }
                }
            }
        }
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "status", "error",
                "timestamp", System.currentTimeMillis(),
                "message", message,
                "code", status.value()
        ));
    }

    /**
     * Endpoint supplémentaire pour vérifier la santé du service
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "HealthProtection API",
                "timestamp", System.currentTimeMillis()
        ));
    }
}