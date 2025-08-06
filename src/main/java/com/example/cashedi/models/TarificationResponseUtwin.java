package com.example.cashedi.models;


import java.util.List;
import lombok.Data;

@Data
public class TarificationResponseUtwin {
    private List<String> messages; // Messages d'erreur ou d'information
    private List<Proposition> propositions; // Liste des propositions tarifées
}