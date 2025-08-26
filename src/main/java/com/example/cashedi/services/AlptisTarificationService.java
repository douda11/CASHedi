package com.example.cashedi.services;

import com.example.cashedi.models.alptis.*;
import com.example.cashedi.models.alptis.devis.AlptisGeneratePdfDevisRequest;
import com.example.cashedi.models.alptis.devis.AlptisGeneratePdfDevisResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service for interacting with the Alptis Tarification API.
 * Similar to ApiviaTarificationService but adapted for Alptis API structure.
 */
@Service
public class AlptisTarificationService {

    private static final Logger logger = LoggerFactory.getLogger(AlptisTarificationService.class);

    private final WebClient webClient;

    @Value("${alptis.api.base-url}")
    private String alptisApiBaseUrl;

    @Value("${alptis.api.key}")
    private String alptisApiKey;

    public AlptisTarificationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Get tarification from Alptis API
     */
    public Mono<AlptisTarificationResponseComplete> getTarification(AlptisTarificationRequest request) {
        try {
            logger.info("Calling Alptis Tarification API with request: {}", request);
            
            // Log the actual JSON payload being sent
            ObjectMapper mapper = new ObjectMapper();
            try {
                String jsonPayload = mapper.writeValueAsString(request);
                logger.info("Actual JSON payload being sent to Alptis API: {}", jsonPayload);
            } catch (Exception e) {
                logger.warn("Could not serialize request to JSON for logging", e);
            }

            return webClient.post()
                    .uri(alptisApiBaseUrl + "/tarification/obtenir-tarifs")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header("X-Alptis-Api-Key", alptisApiKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(AlptisTarificationResponseComplete.class)
                    .doOnSuccess(response -> logger.info("Successfully retrieved Alptis tarification: {}", response))
                    .doOnError(error -> logger.error("Error calling Alptis tarification API: {}", error.getMessage(), error));

        } catch (Exception e) {
            logger.error("Error preparing Alptis tarification request", e);
            return Mono.error(e);
        }
    }

    /**
     * Generate PDF devis from Alptis API
     */
    public Mono<AlptisGeneratePdfDevisResponse> generatePdfDevis(AlptisGeneratePdfDevisRequest request) {
        return getTarifAndGeneratePdf(request, false);
    }

    /**
     * Test generate PDF devis from Alptis API
     */
    public Mono<AlptisGeneratePdfDevisResponse> testGeneratePdfDevis(AlptisGeneratePdfDevisRequest request) {
        return getTarifAndGeneratePdf(request, true);
    }

    /**
     * Get tarification first, then generate PDF with the correct tarif
     */
    private Mono<AlptisGeneratePdfDevisResponse> getTarifAndGeneratePdf(AlptisGeneratePdfDevisRequest request, boolean isTest) {
        // Validate request data before calling API
        String validationError = validateDevisRequest(request);
        if (validationError != null) {
            return Mono.error(new RuntimeException("Validation error: " + validationError));
        }

        // Step 1: Call Tarification API to get the correct rate
        AlptisTarificationRequest tarifRequest = buildTarificationRequestFromDevis(request);

        return getTarification(tarifRequest).flatMap(tarifResponse -> {
            // Step 2: Extract the tarif from the response and call PDF generation
            try {
                if (tarifResponse.getResultatsTarification() != null && !tarifResponse.getResultatsTarification().isEmpty()) {
                    AlptisResultatTarification firstResult = tarifResponse.getResultatsTarification().get(0);
                    AlptisTarifs tarifs = firstResult.getTarifs();
                    
                    if (tarifs != null && tarifs.getTotalMensuel() != null) {
                        request.setTarifMensuel(tarifs.getTotalMensuel());
                        request.setDroitEntree(tarifs.getDroitEntree());
                        
                        return callGeneratePdfApi(request, isTest);
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to parse tarif from Alptis response", e);
            }
            return Mono.error(new RuntimeException("Could not retrieve a valid tarif from Alptis API. Response: " + tarifResponse));
        });
    }

    /**
     * Build tarification request from devis request
     */
    private AlptisTarificationRequest buildTarificationRequestFromDevis(AlptisGeneratePdfDevisRequest devisRequest) {
        AlptisTarificationRequest tarifRequest = new AlptisTarificationRequest();
        tarifRequest.setDateEffet(devisRequest.getDateEffet());

        // Build assures
        AlptisAssures assures = new AlptisAssures();
        assures.setAdherent(devisRequest.getAdherent());
        assures.setConjoint(devisRequest.getConjoint());
        assures.setEnfants(devisRequest.getEnfants());
        tarifRequest.setAssures(assures);

        // Build combinaisons
        AlptisCombinaisonRequest combinaison = new AlptisCombinaisonRequest();
        combinaison.setNumero(1);
        
        AlptisOffre offre = new AlptisOffre();
        offre.setNiveau(devisRequest.getNiveauOffre());
        offre.setSurComplementaire(devisRequest.isSurComplementaire());
        combinaison.setOffre(offre);
        
        combinaison.setCommissionnement(devisRequest.getCommissionnement());
        
        tarifRequest.setCombinaisons(java.util.Arrays.asList(combinaison));

        return tarifRequest;
    }

    /**
     * Call Alptis PDF generation API
     */
    private Mono<AlptisGeneratePdfDevisResponse> callGeneratePdfApi(AlptisGeneratePdfDevisRequest request, boolean isTest) {
        try {
            String endpoint = isTest ? "/devis/generer-pdf-test" : "/devis/generer-pdf";
            
            logger.info("Calling Alptis PDF Generation API at: {} with request: {}", endpoint, request);

            return webClient.post()
                    .uri(alptisApiBaseUrl + endpoint)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header("X-Alptis-Api-Key", alptisApiKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(AlptisGeneratePdfDevisResponse.class)
                    .doOnSuccess(response -> logger.info("Successfully called Alptis PDF generation API."))
                    .doOnError(error -> logger.error("Error calling Alptis PDF generation API: {}", error.getMessage(), error));

        } catch (Exception e) {
            logger.error("Error preparing Alptis PDF generation request", e);
            return Mono.error(e);
        }
    }

    /**
     * Validate devis request data
     */
    private String validateDevisRequest(AlptisGeneratePdfDevisRequest request) {
        // Date d'effet validation (simple regex for YYYY-MM-DD)
        if (request.getDateEffet() == null || !request.getDateEffet().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return "Invalid date format for dateEffet. Expected YYYY-MM-DD.";
        }

        if (request.getSouscripteur() == null) {
            return "Souscripteur is missing.";
        }

        if (request.getConseiller() == null) {
            return "Conseiller is missing.";
        }

        if (request.getAdherent() == null) {
            return "Adherent is missing.";
        }

        // Validate souscripteur address
        if (request.getSouscripteur().getAdresse() == null) {
            return "Adresse for souscripteur is missing.";
        }

        // Code Postal validation
        if (request.getSouscripteur().getAdresse().getCodePostal() == null || 
            !request.getSouscripteur().getAdresse().getCodePostal().matches("^\\d{5}$")) {
            return "Invalid codePostal. Expected 5 digits.";
        }

        // Required address fields validation
        if (request.getSouscripteur().getAdresse().getNomVoie() == null || 
            request.getSouscripteur().getAdresse().getNomVoie().isEmpty()) {
            return "nomVoie is required in the address.";
        }
        
        if (request.getSouscripteur().getAdresse().getNatureVoie() == null || 
            request.getSouscripteur().getAdresse().getNatureVoie().isEmpty()) {
            return "natureVoie is required in the address.";
        }
        
        if (request.getSouscripteur().getAdresse().getVille() == null || 
            request.getSouscripteur().getAdresse().getVille().isEmpty()) {
            return "ville is required in the address.";
        }

        return null; // No validation errors
    }

    /**
     * Get available products/offers from Alptis API
     */
    public Mono<AlptisTarificationResponseComplete> getOffres() {
        logger.info("Calling Alptis for offers list...");

        return webClient.get()
                .uri(alptisApiBaseUrl + "/offres")
                .header("X-Alptis-Api-Key", alptisApiKey)
                .retrieve()
                .bodyToMono(AlptisTarificationResponseComplete.class)
                .doOnSuccess(response -> logger.info("Successfully retrieved offers list."))
                .doOnError(error -> logger.error("Error fetching offers list from Alptis: {}", error.getMessage()));
    }
}
