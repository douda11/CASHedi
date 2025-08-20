package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conjoint {
    private String date_naissance;
    private String code_regime_obligatoire;
    private String nom;
    private String prenom;
    private String numero_assure_social;
    private String code_organisme_affiliation;
    private String code_categorie_socio_professionnelle;
}
