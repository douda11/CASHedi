package com.example.cashedi.services;

import com.example.cashedi.Config.AprilApiConfig;
import com.example.cashedi.models.AprilProfessionalCategory;
import com.example.cashedi.models.AprilProfession;
import com.example.cashedi.models.AprilProfessionalStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
public class AprilServiceImpl implements AprilService {

    private final WebClient webClient;
    private final AprilApiConfig aprilApiConfig;
    private final OAuthService oAuthService;

    @Autowired
    public AprilServiceImpl(WebClient.Builder webClientBuilder, AprilApiConfig aprilApiConfig, OAuthService oAuthService) {
        this.aprilApiConfig = aprilApiConfig;
        this.oAuthService = oAuthService;
        this.webClient = webClientBuilder.baseUrl(aprilApiConfig.getApiRepositoryBaseUrl()).build();
    }

    private String generateRandomUuid() {
        return UUID.randomUUID().toString();
    }

    @Override
    public List<AprilProfessionalCategory> getProfessionalCategories() {
        try {
            String token = oAuthService.getOAuthToken();
            String projectUuid = generateRandomUuid();
            String fullUrl = aprilApiConfig.getApiRepositoryBaseUrl() + "/professionalCategories";
            System.out.println("Calling April API: " + fullUrl);
            
            return webClient.get()
                    .uri("/professionalCategories")
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/json")
                    .header("Cache-Control", "no-cache")
                    .header("x-projectUuid", projectUuid)
                    .header("x-api-version", "1.0")
                    .retrieve()
                    .bodyToFlux(AprilProfessionalCategory.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            System.err.println("Error calling April API for professional categories: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch professional categories from April API", e);
        }
    }

    @Override
    public List<AprilProfession> getProfessions() {
        try {
            String token = oAuthService.getOAuthToken();
            String projectUuid = generateRandomUuid();
            String fullUrl = aprilApiConfig.getApiRepositoryBaseUrl() + "/professions";
            System.out.println("Calling April API: " + fullUrl);
            
            return webClient.get()
                    .uri("/professions")
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/json")
                    .header("Cache-Control", "no-cache")
                    .header("x-projectUuid", projectUuid)
                    .header("x-api-version", "1.0")
                    .retrieve()
                    .bodyToFlux(AprilProfession.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            System.err.println("Error calling April API for professions: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch professions from April API", e);
        }
    }

    @Override
    public List<AprilProfessionalStatus> getProfessionalStatuses(String productCode) {
        try {
            String token = oAuthService.getOAuthToken();
            String projectUuid = generateRandomUuid();
            String fullUrl = aprilApiConfig.getApiRepositoryBaseUrl() + "/products/" + productCode + "/professionalStatuses";
            System.out.println("Calling April API: " + fullUrl);
            
            return webClient.get()
                    .uri("/products/{productCode}/professionalStatuses", productCode)
                    .header("Authorization", "Bearer " + token)
                    .header("Accept", "application/json")
                    .header("Cache-Control", "no-cache")
                    .header("x-projectUuid", projectUuid)
                    .header("x-api-version", "1.0")
                    .retrieve()
                    .bodyToFlux(AprilProfessionalStatus.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            System.err.println("Error calling April API for professional statuses: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch professional statuses from April API", e);
        }
    }
}
