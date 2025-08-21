package com.example.cashedi.services;

import com.example.cashedi.entites.Projet;
import com.example.cashedi.models.Produit;

import java.util.Optional;

public interface ProjetService {
    Projet createProject(Projet projet);
    Optional<Projet> getProjectById(String projetId);
    
    // Method to update project with selected product after user choice
    Projet selectProductForProject(String projetId, Produit selectedProduct);
    
    Projet updateProject(String projetId, Projet projetDetails);
    // We will implement document-related methods later
}
