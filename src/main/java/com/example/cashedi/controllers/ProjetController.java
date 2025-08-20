package com.example.cashedi.controllers;

import com.example.cashedi.entites.Projet;
import com.example.cashedi.services.ProjetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/projets")
public class ProjetController {

    @Autowired
    private ProjetService projetService;

    @PostMapping
        public ResponseEntity<?> createProject(@Valid @RequestBody Projet projet) {
        Projet createdProjet = projetService.createProject(projet);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Le projet n° " + createdProjet.getId() + " a été créé avec succès.");
        response.put("id", createdProjet.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{projetId}")
    public ResponseEntity<Projet> getProjectById(@PathVariable String projetId) {
        return projetService.getProjectById(projetId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{projetId}")
    public ResponseEntity<?> updateProject(@PathVariable String projetId, @RequestBody Projet projetDetails) {
        Projet updatedProjet = projetService.updateProject(projetId, projetDetails);
        if (updatedProjet != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Le projet n° " + updatedProjet.getId() + " a été modifié avec succès.");
            response.put("id", updatedProjet.getId());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Document-related endpoints will be added later
}
