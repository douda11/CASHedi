package com.example.cashedi.services;

import com.example.cashedi.entites.Projet;

import java.util.Optional;

public interface ProjetService {
    Projet createProject(Projet projet);
    Optional<Projet> getProjectById(String projetId);
    Projet updateProject(String projetId, Projet projetDetails);
    // We will implement document-related methods later
}
