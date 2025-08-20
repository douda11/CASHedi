package com.example.cashedi.services;

import com.example.cashedi.entites.Projet;
import com.example.cashedi.models.Tarifs;

public interface AlptisService {
    Tarifs getTarification(Projet projet);
}
