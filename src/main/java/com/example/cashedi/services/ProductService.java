package com.example.cashedi.services;

import com.example.cashedi.models.ProductReference; // Make sure to import ProductReference here
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference; // Important for deserializing List<ProductReference>
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final AprilApiService aprilApiService;

    public ProductService(AprilApiService aprilApiService) {
        this.aprilApiService = aprilApiService;
    }

    public List<ProductReference> getAllProducts() {
        logger.info("Attempting to retrieve the list of reference products.");
        try {
            // The APRIL API returns a list of products. We use ProductReference DTO here.
            return aprilApiService.getFromApi("/products", new ParameterizedTypeReference<List<ProductReference>>() {});
        } catch (RuntimeException e) {
            logger.error("Error retrieving the list of reference products via ProductService: {}", e.getMessage(), e);
            throw new RuntimeException("Could not retrieve the list of reference products via ProductService.", e);
        }
    }

    // You could add other methods here for other "repository" resources
    // for example getCountries(), getProfessions(), etc.
    public List<Map<String, Object>> getAllCountries() {
        logger.info("Attempting to retrieve the list of countries.");
        try {
            // Assuming countries might have a different structure or you haven't defined a specific DTO for them yet
            // The API for /countries returns a list of objects with countryCode and countryTitle
            return aprilApiService.getFromApi("/countries", new ParameterizedTypeReference<List<Map<String, Object>>>() {});
        } catch (RuntimeException e) {
            logger.error("Error retrieving the list of countries via ProductService: {}", e.getMessage(), e);
            throw new RuntimeException("Could not retrieve the list of countries via ProductService.", e);
        }
    }
}
