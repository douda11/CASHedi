package com.example.cashedi.services;

import com.example.cashedi.entites.Projet;
import com.example.cashedi.models.AlptisTarificationResponse;

public interface AlptisService {
    AlptisTarificationResponse getTarification(Projet projet);
    String createAlptisProject(Projet projet);
}
