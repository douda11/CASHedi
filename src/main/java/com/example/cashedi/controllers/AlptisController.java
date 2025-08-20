package com.example.cashedi.controllers;

import com.example.cashedi.entites.Projet;
import com.example.cashedi.models.Tarifs;
import com.example.cashedi.services.AlptisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alptis/sante-pro-plus")
public class AlptisController {

    private final AlptisService alptisService;

    public AlptisController(AlptisService alptisService) {
        this.alptisService = alptisService;
    }

    @PostMapping("/tarification/obtenir-tarifs")
    public ResponseEntity<?> getTarifs(@RequestBody Projet projet) {
        Tarifs tarifs = alptisService.getTarification(projet);
        if (tarifs != null) {
            return ResponseEntity.ok(tarifs);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching tarification.");
    }
}
