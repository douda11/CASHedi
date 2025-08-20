package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {
    private String id;
    private String code_distributeur;
    private String code_partenaire;
    private String email;
    private String nom;
    private String prenom;
    private String username;
}
