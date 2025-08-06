package com.example.cashedi.services;

import com.example.cashedi.Config.AprilApiConfig;
import com.example.cashedi.Config.UtwinApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID; // Importation de UUID nécessaire

@Service
public class    AprilApiService {

    private static final Logger logger = LoggerFactory.getLogger(AprilApiService.class);
    private final RestTemplate restTemplate;
    private final OAuthService oAuthService;
    private final AprilApiConfig aprilApiConfig;
    private final UtwinApiConfig utwinApiConfig; // Ajout de la configuration UTWIN

    public AprilApiService(RestTemplate restTemplate, OAuthService oAuthService, AprilApiConfig aprilApiConfig, UtwinApiConfig utwinApiConfig) {
        this.restTemplate = restTemplate;
        this.oAuthService = oAuthService;
        this.aprilApiConfig = aprilApiConfig;
        this.utwinApiConfig = utwinApiConfig; // Injection de la configuration UTWIN
    }

    /**
     * Génère un UUID aléatoire pour utilisation comme identifiant de traçabilité.
     * Cette méthode est utilisée pour le x-projectUuid.
     */
    private String generateRandomUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * Effectue un appel POST générique à l'API APRIL pour un calcul de tarif.
     *
     * @param requestBody Le corps de la requête JSON (le projet à tarifer).
     * @param pricingType Le type de tarification (ex: Simple, AllOptions).
     * @param withSchedule Indique si l'échéancier doit être inclus (peut être null).
     * @return La réponse de l'API sous forme de Map.
     */
    /**
     * Effectue un appel POST générique à l'API APRIL pour un calcul de tarif.
     *
     * @param requestBody Le corps de la requête JSON (le projet à tarifer).
     * @param pricingType Le type de tarification (ex: Simple, AllOptions).
     * @param withSchedule Indique si l'échéancier doit être inclus (peut être null).
     * @return La réponse de l'API sous forme de liste de Maps.
     */
    public List<Map<String, Object>> calculateTarif(Map<String, Object> requestBody, String pricingType, Boolean withSchedule) {
        // 1. Authentification: Obtenir le token OAuth
        String token = oAuthService.getOAuthToken();
        if (token == null || token.isEmpty()) {
            logger.error("Impossible d'obtenir le token OAuth pour le calcul de tarif.");
            throw new RuntimeException("Échec d'authentification: token absent pour le calcul de tarif.");
        }

        // 2. Générer un identifiant de projet unique pour la traçabilité
        String currentProjectUuid = generateRandomUuid();
        logger.debug("Using x-projectUuid for calculateTarif: {}", currentProjectUuid);

        // 3. Configuration des headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        headers.set("Cache-Control", "no-cache");
        headers.set("x-projectUuid", currentProjectUuid);
        headers.set("x-api-version", "1.0"); // Ajout important selon la doc APRIL

        // 4. Construction de l'URL de l'API de tarification
        String url = String.format("%s/prices?pricingType=%s",
                aprilApiConfig.getApiPricingBaseUrl(),
                pricingType.toLowerCase()); // Assurez-vous que c'est en minuscules

        if (withSchedule != null) {
            url += "&withSchedule=" + withSchedule;
        }

        logger.info("Calcul du tarif avec l'URL : {}", url);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // 5. Envoi de la requête et gestion des erreurs
        try {
            ParameterizedTypeReference<List<Map<String, Object>>> responseType =
                    new ParameterizedTypeReference<>() {};

            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    responseType
            );

            logger.debug("Réponse reçue pour le calcul de tarif - Status: {}", response.getStatusCode());
            return response.getBody();

        } catch (HttpClientErrorException e) {
            String errorDetails = String.format(
                    "Erreur client HTTP lors du calcul de tarif - URL: %s | Status: %s | Body: %s",
                    url,
                    e.getStatusCode(),
                    e.getResponseBodyAsString()
            );
            logger.error(errorDetails);
            throw new RuntimeException("Erreur client API lors du calcul de tarif: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors du calcul du tarif pour l'URL {}: {}", url, e.getMessage(), e);
            throw new RuntimeException("Erreur inattendue lors du calcul du tarif.", e);
        }
    }

    /**
     * Effectue un appel GET générique à l'API APRIL pour les ressources de référentiel.
     *
     * @param path Le chemin de l'API (ex: "/products", "/countries").
     * @param responseType Le type de la réponse attendue, encapsulé dans ParameterizedTypeReference
     * pour gérer les types génériques comme List<ProductReference>.
     * @param <T> Le type générique de la réponse.
     * @return La réponse de l'API.
     */
    /**
     * Crée une demande de devis (Quotation) ou une mise en relation (ContactRequest).
     *
     * @param requestBody Le corps de la requête JSON (le projet).
     * @param requestType Le type de requête ('Quotation' ou 'ContactRequest').
     * @return La réponse de l'API sous forme de Map.
     */
    public Map<String, Object> createProject(Map<String, Object> requestBody, String requestType) {
        String token = oAuthService.getOAuthToken();
        if (token == null || token.isEmpty()) {
            logger.error("Impossible d'obtenir le token OAuth pour la création de projet.");
            throw new RuntimeException("Échec d'authentification: token absent.");
        }

        String currentProjectUuid = generateRandomUuid();
        logger.debug("Using x-projectUuid for createProject: {}", currentProjectUuid);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        headers.set("x-projectUuid", currentProjectUuid);
        headers.set("x-api-version", "1.0");

        String url = String.format("%s?marketingParameters.requestType=%s",
                aprilApiConfig.getApiProjectsBaseUrl(), // Utilisation de la nouvelle URL dédiée
                requestType);

        // Ajout d'une journalisation détaillée du corps de la requête pour le débogage
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String jsonBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestBody);
            logger.info("Corps de la requête pour la création de projet :\n{}", jsonBody);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            logger.error("Erreur lors de la sérialisation du corps de la requête en JSON", e);
        }



        logger.info("Création de projet avec l'URL : {}", url);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<>() {};

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    responseType
            );

            logger.debug("Réponse reçue pour la création de projet - Status: {}", response.getStatusCode());
            return response.getBody();

        } catch (HttpClientErrorException e) {
            String errorDetails = String.format(
                    "Erreur client HTTP lors de la création de projet - URL: %s | Status: %s | Body: %s",
                    url,
                    e.getStatusCode(),
                    e.getResponseBodyAsString()
            );
            logger.error(errorDetails);
            throw new RuntimeException("Erreur client API lors de la création de projet: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la création de projet pour l'URL {}: {}", url, e.getMessage(), e);
            throw new RuntimeException("Erreur inattendue lors de la création de projet.", e);
        }
    }

    /**
     * Crée une proposition commerciale en appelant l'API Partenaires Santé (UTWIN).
     *
     * @param requestBody Le corps de la requête JSON (le projet).
     * @param action Le type d'action ('telechargement' ou 'envoiParEmail').
     * @return La réponse de l'API sous forme de Map.
     */
    public Map<String, Object> createCommercialProposal(Map<String, Object> requestBody, String action) {
        logger.info("Création d'une proposition commerciale UTWIN avec action: {}", action);

        // 1. Configuration des headers spécifiques à l'API UTWIN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");
        headers.set("X-U-Licence", utwinApiConfig.getLicence());
        headers.set("X-Utwin-Contexte-Vendeur", utwinApiConfig.getContexteVendeur());

        // 2. Construction de l'URL de l'API
        String url = String.format("%s/api/Sante/v1/PropositionCommerciale?action=%s",
                utwinApiConfig.getBaseUrl(),
                action);

        logger.info("Appel de l'API PropositionCommerciale UTWIN sur l'URL : {}", url);
        logger.info("Payload envoyé à UTWIN : {}", requestBody);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // 3. Envoi de la requête et gestion des erreurs
        try {
            ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<>() {};

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    responseType
            );

            logger.debug("Réponse reçue pour la proposition commerciale - Status: {}", response.getStatusCode());
            return response.getBody();

        } catch (HttpClientErrorException e) {
            String errorDetails = String.format(
                    "Erreur client HTTP lors de la création de la proposition commerciale UTWIN - URL: %s | Status: %s | Body: %s",
                    url,
                    e.getStatusCode(),
                    e.getResponseBodyAsString()
            );
            logger.error(errorDetails);
            throw new RuntimeException("Erreur client API lors de la création de la proposition commerciale: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la création de la proposition commerciale pour l'URL {}: {}", url, e.getMessage(), e);
            throw new RuntimeException("Erreur inattendue lors de la création de la proposition commerciale.", e);
        }
    }

    /**
     * Récupère un document depuis l'API Partenaires Santé (UTWIN) par son identifiant.
     *
     * @param identifiant L'identifiant technique du document.
     * @return Le contenu du document sous forme de tableau d'octets (byte[]).
     */
    public byte[] getDocument(String identifiant) {
        logger.info("Récupération du document UTWIN avec l'identifiant: {}", identifiant);

        // 1. Configuration des headers spécifiques à l'API UTWIN
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/pdf"); // On s'attend à recevoir un PDF
        headers.set("X-U-Licence", utwinApiConfig.getLicence());
        headers.set("X-Utwin-Contexte-Vendeur", utwinApiConfig.getContexteVendeur());

        // 2. Construction de l'URL de l'API
        String url = String.format("%s/api/Sante/v1/Documents/%s",
                utwinApiConfig.getBaseUrl(),
                identifiant);

        logger.info("Appel de l'API de récupération de document UTWIN sur l'URL : {}", url);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 3. Envoi de la requête et gestion des erreurs
        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    byte[].class // On attend un tableau d'octets en réponse
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                logger.debug("Document récupéré avec succès - Status: {}", response.getStatusCode());
                return response.getBody();
            } else {
                logger.error("Réponse inattendue de l'API document UTWIN: Status {} | Body est null: {}", response.getStatusCode(), response.getBody() == null);
                throw new RuntimeException("N'a pas pu récupérer le document. Status: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException e) {
            String errorDetails = String.format(
                    "Erreur client HTTP lors de la récupération du document UTWIN - URL: %s | Status: %s | Body: %s",
                    url,
                    e.getStatusCode(),
                    e.getResponseBodyAsString()
            );
            logger.error(errorDetails);
            throw new RuntimeException("Erreur client API lors de la récupération du document: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la récupération du document pour l'URL {}: {}", url, e.getMessage(), e);
            throw new RuntimeException("Erreur inattendue lors de la récupération du document.", e);
        }
    }

    public <T> T getFromApi(String path, ParameterizedTypeReference<T> responseType) {
        // 1. Authentification: Obtenir le token OAuth
        String token = oAuthService.getOAuthToken();
        if (token == null || token.isEmpty()) {
            logger.error("Token OAuth null ou vide");
            throw new RuntimeException("Échec d'authentification: token absent");
        }

        // 2. Générer un identifiant de projet unique pour la traçabilité
        String currentProjectUuid = generateRandomUuid();
        logger.debug("Using x-projectUuid for getFromApi: {}", currentProjectUuid);

        // 3. Configuration des headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // Ajout de l'en-tête d'autorisation
        headers.set("Accept", "application/json");
        headers.set("Cache-Control", "no-cache");
        headers.set("x-projectUuid", currentProjectUuid); // En-tête de traçabilité spécifique à APRIL

        logger.debug("Headers configurés: {}", headers);

        // 4. Construction de l'URL complète
        String url = aprilApiConfig.getApiRepositoryBaseUrl() + path;
        logger.info("Requête GET vers: {}", url);

        // 5. Envoi de la requête avec gestion d'erreur améliorée
        try {
            ResponseEntity<T> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    responseType
            );

            logger.debug("Réponse reçue - Status: {}", response.getStatusCode());
            return response.getBody();

        } catch (HttpClientErrorException e) {
            String errorDetails = String.format(
                    "Erreur client HTTP lors de l'appel API GET - URL: %s | Status: %s | Body: %s",
                    url,
                    e.getStatusCode(),
                    e.getResponseBodyAsString()
            );
            logger.error(errorDetails);

            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new RuntimeException("Permissions insuffisantes. Vérifiez les scopes du token ou l'UUID de projet.", e);
            }
            throw new RuntimeException("Erreur client API GET: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);

        } catch (Exception e) {
            logger.error("Erreur inattendue lors de l'appel API GET pour l'URL {}: {}", url, e.getMessage(), e);
            throw new RuntimeException("Erreur système temporaire lors de l'appel API GET.", e);
        }
    }
}
