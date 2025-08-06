package com.example.cashedi.controllers;

import com.example.cashedi.models.ProductReference; // Importez ProductReference
import com.example.cashedi.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api") // Ou /api/products selon votre préférence
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProducts() {
        logger.info("Requête reçue pour la liste des produits de référence.");
        try {
            // Modifié pour utiliser List<ProductReference> comme type de retour attendu
            List<ProductReference> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (RuntimeException e) {
            logger.error("Erreur lors de la récupération des produits de référence via le contrôleur : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne du serveur lors de la récupération des produits: " + e.getMessage());
        }
    }

    @GetMapping("/countries")
    public ResponseEntity<?> getCountries() {
        logger.info("Requête reçue pour la liste des pays.");
        try {
            List<Map<String, Object>> countries = productService.getAllCountries();
            return ResponseEntity.ok(countries);
        } catch (RuntimeException e) {
            logger.error("Erreur lors de la récupération des pays via le contrôleur : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne du serveur lors de la récupération des pays: " + e.getMessage());
        }
    }
}
