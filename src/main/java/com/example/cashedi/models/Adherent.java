package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adherent {
    @NotBlank(message = "La date de naissance ne peut pas être vide")
    private String date_naissance;
    @NotBlank(message = "Le code du régime obligatoire ne peut pas être vide")
    private String code_regime_obligatoire;
    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nom;
    @NotBlank(message = "Le prénom ne peut pas être vide")
    private String prenom;
    @NotBlank(message = "Le numéro d'assuré social ne peut pas être vide")
    private String numero_assure_social;
    private String code_organisme_affiliation;
    private String code_categorie_socio_professionnelle;
    private String code_civilite;
    @NotNull(message = "Les coordonnées ne peuvent pas être nulles")
    @Valid
    private Coordonnees coordonnees;
    private String code_situation_familiale;
    private String code_cadre_exercice;
    private String siret;
    private boolean benefice_loi_madelin;
    private boolean micro_entrepreneur;
    private String code_statut_professionnel;
    @NotNull(message = "Les optins ne peuvent pas être nuls")
    @Valid
    private Optins optins;
}
