package com.example.cashedi.controllers;

import com.example.cashedi.dto.ResultatComparaison;
import com.example.cashedi.entites.BesoinClient;
import com.example.cashedi.services.ComparateurService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comparateur")
public class ComparateurController {

    private final ComparateurService comparateurService;

    public ComparateurController(ComparateurService comparateurService) {
        this.comparateurService = comparateurService;
    }

    @PostMapping
    public List<ResultatComparaison> comparer(@RequestBody BesoinClient besoin) {
        return comparateurService.comparerFormules(besoin);
    }
}