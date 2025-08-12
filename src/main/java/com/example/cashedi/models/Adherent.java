package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adherent {
    private String date_naissance;
    private String code_regime_obligatoire;
    private String nom;
    private String prenom;
    private String numero_assure_social;
    private String code_organisme_affiliation;
    private String code_categorie_socio_professionnelle;
    private String code_civilite;
    private Coordonnees coordonnees;
    private String code_situation_familiale;
    private String code_cadre_exercice;
    private String siret;
    private boolean benefice_loi_madelin;
    private boolean micro_entrepreneur;
    private String code_statut_professionnel;
    private Optins optins;
}
