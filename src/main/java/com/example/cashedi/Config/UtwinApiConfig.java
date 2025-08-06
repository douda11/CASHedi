package com.example.cashedi.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "utwin.api")
public class UtwinApiConfig {

    private String baseUrl;
    private String licence;
    private String contexteVendeur;

    // Getters and Setters

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getContexteVendeur() {
        return contexteVendeur;
    }

    public void setContexteVendeur(String contexteVendeur) {
        this.contexteVendeur = contexteVendeur;
    }
}
