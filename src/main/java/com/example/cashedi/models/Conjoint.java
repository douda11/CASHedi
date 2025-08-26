package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conjoint {
    private String date_naissance;
    @JsonProperty("regime_obligatoire")
    private String code_regime_obligatoire;
    private String nom;
    private String prenom;
    private String numero_assure_social;
    private String code_organisme_affiliation;
    @JsonProperty("categorie_socioprofessionnelle")
    private String code_categorie_socio_professionnelle;
}
