package com.example.cashedi.controllers;

import com.example.cashedi.entites.Projet;
import com.example.cashedi.models.Tarifs;
import com.example.cashedi.models.AlptisTarificationResponse;
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

    @PostMapping("/tarification")
    public ResponseEntity<AlptisTarificationResponse> getTarification(@RequestBody Projet projet) {
        try {
            AlptisTarificationResponse response = alptisService.getTarification(projet);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
