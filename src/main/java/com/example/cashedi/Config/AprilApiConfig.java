package com.example.cashedi.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuration pour les URLs de base de l'API APRIL.
 * Les valeurs sont injectées depuis application.properties.
 */
@Configuration
public class AprilApiConfig {

    // URL de base pour les routes de référentiel (GET /products, /countries, etc.)
    @Value("${april.api.repository-base-url}")
    private String apiRepositoryBaseUrl;

    // URL de base pour les routes de tarification (POST /projects/prices)
    @Value("${april.api.pricing-base-url}")
    private String apiPricingBaseUrl;

    // URL de base pour les routes de projet (POST /projects)
    @Value("${april.api.projects-base-url}")
    private String apiProjectsBaseUrl;

    // Getters pour accéder aux URLs
    public String getApiRepositoryBaseUrl() {
        return apiRepositoryBaseUrl;
    }

    public String getApiPricingBaseUrl() {
        return apiPricingBaseUrl;
    }

    public String getApiProjectsBaseUrl() {
        return apiProjectsBaseUrl;
    }

    // Setters (optionnels, souvent non nécessaires pour les classes de configuration)
    public void setApiRepositoryBaseUrl(String apiRepositoryBaseUrl) {
        this.apiRepositoryBaseUrl = apiRepositoryBaseUrl;
    }

    public void setApiPricingBaseUrl(String apiPricingBaseUrl) {
        this.apiPricingBaseUrl = apiPricingBaseUrl;
    }

    public void setApiProjectsBaseUrl(String apiProjectsBaseUrl) {
        this.apiProjectsBaseUrl = apiProjectsBaseUrl;
    }
}
