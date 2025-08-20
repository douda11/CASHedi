package com.example.cashedi.services;

import com.example.cashedi.entites.Projet;
import com.example.cashedi.models.Tarifs;
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
    public Tarifs getTarification(Projet projet) {
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
            
            return parseResponse(response.getBody(), projet.getOffre().getNiveau());
        } catch (Exception e) {
            logger.error("Error calling Alptis tarification API", e);
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

    private Tarifs parseResponse(String responseBody, String niveau) {
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
            
            logger.info("Successfully parsed tarifs: totalMensuel={}, cotisationBase={}, cotisationSurco={}", 
                tarifs.getTotalMensuel(), tarifs.getCotisationMensuelleBase(), tarifs.getCotisationMensuelleSurco());

            return tarifs;
        } catch (Exception e) {
            logger.error("Error parsing Alptis tarification response", e);
            return null;
        }
    }
}
