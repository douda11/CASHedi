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
import org.springframework.web.util.UriComponentsBuilder;

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
        body.put("code_distributeur", projet.getUtilisateur().getCode_distributeur());
        body.put("date_effet", new SimpleDateFormat("yyyy-MM-dd").format(projet.getDateEffet()));

        Map<String, Object> assures = new HashMap<>();

        // Adherent
        if (projet.getAssures() != null && projet.getAssures().getAdherent() != null) {
            Map<String, Object> adherent = new HashMap<>();
            
            // Add required fields with null checks
            String dateNaissance = projet.getAssures().getAdherent().getDate_naissance();
            String regimeObligatoire = projet.getAssures().getAdherent().getCode_regime_obligatoire();
            String codePostal = projet.getAssures().getAdherent().getCoordonnees() != null && 
                               projet.getAssures().getAdherent().getCoordonnees().getAdresse() != null ?
                               projet.getAssures().getAdherent().getCoordonnees().getAdresse().getCodePostal() : null;
            String cadreExercice = projet.getAssures().getAdherent().getCode_cadre_exercice();
            String categorieSocioprofessionnelle = projet.getAssures().getAdherent().getCode_categorie_socio_professionnelle();

            // Business rule: For certain professional categories, adjust cadre_exercice
            if ("ARTISANS".equals(categorieSocioprofessionnelle) || 
                "COMMERCANTS".equals(categorieSocioprofessionnelle) ||
                "PROFESSIONS_LIBERALES".equals(categorieSocioprofessionnelle)) {
                cadreExercice = "INDEPENDANT";
                logger.info("Adjusted cadreExercice to INDEPENDANT for categorie: {}", categorieSocioprofessionnelle);
            }
            boolean microEntrepreneur = projet.getAssures().getAdherent().isMicro_entrepreneur();
            
            logger.info("Raw adherent data: dateNaissance={}, regimeObligatoire={}, codePostal={}, cadreExercice={}, categorieSocioprofessionnelle={}, microEntrepreneur={}", 
                dateNaissance, regimeObligatoire, codePostal, cadreExercice, categorieSocioprofessionnelle, microEntrepreneur);
            
            // Use snake_case field names as expected by the API
            adherent.put("date_naissance", dateNaissance);
            adherent.put("regime_obligatoire", regimeObligatoire);
            adherent.put("code_postal", codePostal);
            adherent.put("cadre_exercice", cadreExercice);
            adherent.put("categorie_socioprofessionnelle", categorieSocioprofessionnelle);
            adherent.put("micro_entrepreneur", microEntrepreneur);
            
            assures.put("adherent", adherent);
        }

        // Conjoint
        if (projet.getAssures() != null && projet.getAssures().getConjoint() != null) {
            Map<String, Object> conjoint = new HashMap<>();
            conjoint.put("date_naissance", projet.getAssures().getConjoint().getDate_naissance());
            conjoint.put("regime_obligatoire", projet.getAssures().getConjoint().getCode_regime_obligatoire());
            conjoint.put("categorie_socioprofessionnelle", projet.getAssures().getConjoint().getCode_categorie_socio_professionnelle());
            assures.put("conjoint", conjoint);
        }

        // Enfants
        if (projet.getAssures() != null && projet.getAssures().getEnfants() != null && !projet.getAssures().getEnfants().isEmpty()) {
            java.util.List<Map<String, Object>> enfantsList = new java.util.ArrayList<>();
            for (com.example.cashedi.models.Enfant enfant : projet.getAssures().getEnfants()) {
                Map<String, Object> enfantMap = new HashMap<>();
                enfantMap.put("date_naissance", enfant.getDate_naissance());
                enfantMap.put("regime_obligatoire", enfant.getCode_regime_obligatoire());
                enfantsList.add(enfantMap);
            }
            assures.put("enfants", enfantsList);
        }

        // Add combinaisons based on the offer level
        Map<String, Object> combinaison = new HashMap<>();
        combinaison.put("numero", 1); // Required unique number for each combination
        
        // Create offre object instead of string
        Map<String, Object> offre = new HashMap<>();
        offre.put("niveau", projet.getOffre().getNiveau());
        offre.put("sur_complementaire", projet.getOffre().isSur_complementaire());
        combinaison.put("offre", offre);
        
        combinaison.put("commissionnement", projet.getCommissionnement()); // Required commissioning
        body.put("combinaisons", java.util.Arrays.asList(combinaison));

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

        // Top-level fields from Projet
        projectData.put("code_distributeur", projet.getUtilisateur().getCode_distributeur());
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
