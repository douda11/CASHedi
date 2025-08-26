package com.example.cashedi.services;

import com.example.cashedi.entites.Projet;
import com.example.cashedi.models.Tarifs;
import com.example.cashedi.models.AlptisTarificationResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class AlptisServiceImpl implements AlptisService {

    private static final Logger logger = LoggerFactory.getLogger(AlptisServiceImpl.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${alptis.api.base-url}")
    private String alptisApiBaseUrl;

    @Value("${alptis.api.key}")
    private String alptisApiKey;

    public AlptisServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public AlptisTarificationResponse getTarification(Projet projet) {
        try {
            Map<String, Object> requestBody = buildRequestBody(projet);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Alptis-Api-Key", alptisApiKey);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                alptisApiBaseUrl + "/tarification/obtenir-tarifs",
                HttpMethod.POST,
                entity,
                String.class
            );
            
            return parseResponse(response.getBody());
        } catch (Exception e) {
            logger.error("Error calling Alptis tarification API", e);
            return null;
        }
    }

    // New method to create project in Alptis system
    public String createAlptisProject(Projet projet) {
        try {
            Map<String, Object> projectData = buildAlptisProjectData(projet);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Alptis-Api-Key", alptisApiKey);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(projectData, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                alptisApiBaseUrl + "/projets",
                HttpMethod.POST,
                entity,
                String.class
            );
            
            // Parse response to get Alptis project ID
            JsonNode responseNode = objectMapper.readTree(response.getBody());
            String alptisProjectId = responseNode.path("id").asText();
            
            logger.info("Project created in Alptis system with ID: {}", alptisProjectId);
            return alptisProjectId;
            
        } catch (Exception e) {
            logger.error("Error creating project in Alptis system", e);
            return null;
        }
    }

    private Map<String, Object> buildRequestBody(Projet projet) {
        Map<String, Object> body = new HashMap<>();
        
        // Date effet in yyyy-MM-dd format
        body.put("date_effet", new SimpleDateFormat("yyyy-MM-dd").format(projet.getDateEffet()));

        Map<String, Object> assures = new HashMap<>();

        // Adherent
        if (projet.getAssures() != null && projet.getAssures().getAdherent() != null) {
            Map<String, Object> adherent = new HashMap<>();
            
            // Map fields according to Alptis API format with defaults for required fields
            adherent.put("date_naissance", projet.getAssures().getAdherent().getDate_naissance());
            
            // regime_obligatoire with default
            String regimeObligatoire = projet.getAssures().getAdherent().getCode_regime_obligatoire();
            adherent.put("regime_obligatoire", regimeObligatoire != null ? regimeObligatoire : "SECURITE_SOCIALE");
            
            // cadre_exercice with default
            String cadreExercice = projet.getAssures().getAdherent().getCode_cadre_exercice();
            adherent.put("cadre_exercice", cadreExercice != null ? cadreExercice : "INDEPENDANT");
            
            // categorie_socioprofessionnelle with default
            String categorieSocio = projet.getAssures().getAdherent().getCode_categorie_socio_professionnelle();
            adherent.put("categorie_socioprofessionnelle", categorieSocio != null ? categorieSocio : "AGRICULTEURS_EXPLOITANTS");
            
            adherent.put("micro_entrepreneur", projet.getAssures().getAdherent().isMicro_entrepreneur());
            
            // Add code_postal with default if not available
            String codePostal = null;
            if (projet.getAssures().getAdherent().getCoordonnees() != null && 
                projet.getAssures().getAdherent().getCoordonnees().getAdresse() != null) {
                codePostal = projet.getAssures().getAdherent().getCoordonnees().getAdresse().getCodePostal();
            }
            adherent.put("code_postal", codePostal != null ? codePostal : "69003");
            
            // Add statut_professionnel if available (new field from API example)
            if (projet.getAssures().getAdherent().getCode_statut_professionnel() != null) {
                adherent.put("statut_professionnel", projet.getAssures().getAdherent().getCode_statut_professionnel());
            }
            
            assures.put("adherent", adherent);
        }

        // Conjoint
        if (projet.getAssures() != null && projet.getAssures().getConjoint() != null) {
            Map<String, Object> conjoint = new HashMap<>();
            conjoint.put("date_naissance", projet.getAssures().getConjoint().getDate_naissance());
            
            // regime_obligatoire with default
            String regimeObligatoireConjoint = projet.getAssures().getConjoint().getCode_regime_obligatoire();
            conjoint.put("regime_obligatoire", regimeObligatoireConjoint != null ? regimeObligatoireConjoint : "SECURITE_SOCIALE");
            
            // categorie_socioprofessionnelle with default
            String categorieSocioConjoint = projet.getAssures().getConjoint().getCode_categorie_socio_professionnelle();
            conjoint.put("categorie_socioprofessionnelle", categorieSocioConjoint != null ? categorieSocioConjoint : "AGRICULTEURS_EXPLOITANTS");
            
            assures.put("conjoint", conjoint);
        }

        // Enfants
        if (projet.getAssures() != null && projet.getAssures().getEnfants() != null && !projet.getAssures().getEnfants().isEmpty()) {
            java.util.List<Map<String, Object>> enfantsList = new java.util.ArrayList<>();
            for (com.example.cashedi.models.Enfant enfant : projet.getAssures().getEnfants()) {
                Map<String, Object> enfantMap = new HashMap<>();
                enfantMap.put("date_naissance", enfant.getDate_naissance());
                
                // regime_obligatoire with default
                String regimeObligatoireEnfant = enfant.getCode_regime_obligatoire();
                enfantMap.put("regime_obligatoire", regimeObligatoireEnfant != null ? regimeObligatoireEnfant : "SECURITE_SOCIALE");
                
                enfantsList.add(enfantMap);
            }
            assures.put("enfants", enfantsList);
        }

        // Add combinaisons according to Alptis API format
        java.util.List<Map<String, Object>> combinaisons = new java.util.ArrayList<>();
        
        Map<String, Object> combinaison = new HashMap<>();
        combinaison.put("numero", 1);
        
        // Offre object with null safety check
        Map<String, Object> offre = new HashMap<>();
        if (projet.getOffre() != null) {
            offre.put("niveau", projet.getOffre().getNiveau());
            offre.put("sur_complementaire", projet.getOffre().isSur_complementaire());
        } else {
            // Default values if offre is null
            logger.warn("Offre is null, using default values");
            offre.put("niveau", "NIVEAU_1");
            offre.put("sur_complementaire", false);
        }
        combinaison.put("offre", offre);
        
        // Add ayants_droit based on assures
        Map<String, Object> ayantsDroit = new HashMap<>();
        ayantsDroit.put("conjoint", projet.getAssures() != null && projet.getAssures().getConjoint() != null);
        
        int nombreEnfants = 0;
        if (projet.getAssures() != null && projet.getAssures().getEnfants() != null) {
            nombreEnfants = projet.getAssures().getEnfants().size();
        }
        ayantsDroit.put("enfants", nombreEnfants);
        combinaison.put("ayants_droit", ayantsDroit);
        
        // Add commissionnement with null safety check
        String commissionnement = projet.getCommissionnement();
        if (commissionnement == null || commissionnement.trim().isEmpty()) {
            logger.warn("Commissionnement is null or empty, using default value");
            commissionnement = "LIN0";
        }
        combinaison.put("commissionnement", commissionnement);
        
        combinaisons.add(combinaison);
        body.put("combinaisons", combinaisons);

        body.put("assures", assures);
        
        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            logger.info("Final JSON request body: {}", jsonBody);
        } catch (Exception e) {
            logger.error("Error serializing request body", e);
        }
        
        return body;
    }

    private Map<String, Object> buildAlptisProjectData(Projet projet) {
        Map<String, Object> projectData = new HashMap<>();

        // Based on the tarification request, we can assume a similar structure is needed.
        // The API documentation is the source of truth here.

        // Top-level fields from Projet - no code_distributeur needed
        projectData.put("date_effet", new SimpleDateFormat("yyyy-MM-dd").format(projet.getDateEffet()));
        projectData.put("commissionnement", projet.getCommissionnement());
        projectData.put("type_cotisation", projet.getTypeCotisation());

        // Nested objects
        // Manually build the assures object to ensure correct structure
        Map<String, Object> assures = new HashMap<>();
        if (projet.getAssures() != null && projet.getAssures().getAdherent() != null) {
            Map<String, Object> adherent = objectMapper.convertValue(projet.getAssures().getAdherent(), Map.class);
            assures.put("adherent", adherent);
        }
        projectData.put("assures", assures); 

        projectData.put("offre", projet.getOffre()); // Assuming the Offre object structure is correct
        projectData.put("paiement", projet.getPaiement()); // Assuming the Paiement object structure is correct
        projectData.put("remboursement", projet.getRemboursement());

        // The tarification response fields might be needed for project creation
        Map<String, Object> tarificationResult = new HashMap<>();
        tarificationResult.put("tarifs", projet.getTarifs());
        tarificationResult.put("generation_tarif", projet.getGenerationTarif());
        tarificationResult.put("code_association", projet.getCodeAssociation());
        tarificationResult.put("millesime", projet.getMillesime());

        projectData.put("resultats_tarification", java.util.Arrays.asList(tarificationResult));

        // It's likely that only the selected product's code is needed.
        if (projet.getProduits() != null && !projet.getProduits().isEmpty()) {
            projectData.put("produits", projet.getProduits());
        }

        // Log the final JSON payload for debugging
        try {
            String jsonPayload = objectMapper.writeValueAsString(projectData);
            logger.info("Alptis Create Project Request Body: {}", jsonPayload);
        } catch (Exception e) {
            logger.error("Error serializing Alptis project data", e);
        }

        return projectData;
    }

    private AlptisTarificationResponse parseResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            
            // Log the full response to understand the structure
            logger.info("Full API response: {}", responseBody);
            
            // Parse the actual API response structure
            JsonNode resultatsNode = root.path("resultats_tarification");
            if (resultatsNode.isMissingNode() || !resultatsNode.isArray() || resultatsNode.size() == 0) {
                logger.error("Could not find resultats_tarification in response");
                return null;
            }

            JsonNode firstResult = resultatsNode.get(0);
            JsonNode tarifsNode = firstResult.path("tarifs");

            if (tarifsNode.isMissingNode()) {
                logger.error("Could not find tarifs in response");
                return null;
            }

            // Extract tarif data from the actual response structure
            Tarifs tarifs = new Tarifs();
            tarifs.setTotalMensuel(tarifsNode.path("total_mensuel").asDouble());
            tarifs.setCotisationMensuelleBase(tarifsNode.path("cotisation_mensuelle_base").asDouble());
            tarifs.setCotisationMensuelleSurco(tarifsNode.path("cotisation_mensuelle_surco").asDouble());
            tarifs.setCotisationAssociation(tarifsNode.path("cotisation_association").asDouble());
            tarifs.setCotisationDeductibleMadelin(tarifsNode.path("cotisation_deductible_madelin").asDouble());
            tarifs.setDroitEntree(tarifsNode.path("droit_entree").asDouble());
            
            // Extract additional fields from the first result
            String generationTarif = firstResult.path("generation_tarif").asText();
            String codeAssociation = firstResult.path("code_association").asText();
            String typeCotisation = firstResult.path("type_cotisation").asText();
            int millesime = firstResult.path("millesime").asInt();
            
            logger.info("Successfully parsed tarifs: totalMensuel={}, cotisationBase={}, cotisationSurco={}", 
                tarifs.getTotalMensuel(), tarifs.getCotisationMensuelleBase(), tarifs.getCotisationMensuelleSurco());
            logger.info("Additional fields: generationTarif={}, codeAssociation={}, typeCotisation={}, millesime={}", 
                generationTarif, codeAssociation, typeCotisation, millesime);

            // Create and return the complete response
            AlptisTarificationResponse response = new AlptisTarificationResponse();
            response.setTarifs(tarifs);
            response.setGenerationTarif(generationTarif);
            response.setCodeAssociation(codeAssociation);
            response.setTypeCotisation(typeCotisation);
            response.setMillesime(millesime);

            return response;
        } catch (Exception e) {
            logger.error("Error parsing Alptis tarification response", e);
            return null;
        }
    }
}
