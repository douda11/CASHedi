package com.example.cashedi.models.alptis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlptisConjoint {

    @JsonProperty("date_naissance")
    private String dateNaissance;

    @JsonProperty("regime_obligatoire")
    private String regimeObligatoire;

    @JsonProperty("categorie_socioprofessionnelle")
    private String categorieSocioprofessionnelle;

    // Constructors
    public AlptisConjoint() {}

    public AlptisConjoint(String dateNaissance, String regimeObligatoire, String categorieSocioprofessionnelle) {
        this.dateNaissance = dateNaissance;
        this.regimeObligatoire = regimeObligatoire;
        this.categorieSocioprofessionnelle = categorieSocioprofessionnelle;
    }

    // Getters and Setters
    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getRegimeObligatoire() {
        return regimeObligatoire;
    }

    public void setRegimeObligatoire(String regimeObligatoire) {
        this.regimeObligatoire = regimeObligatoire;
    }

    public String getCategorieSocioprofessionnelle() {
        return categorieSocioprofessionnelle;
    }

    public void setCategorieSocioprofessionnelle(String categorieSocioprofessionnelle) {
        this.categorieSocioprofessionnelle = categorieSocioprofessionnelle;
    }

    @Override
    public String toString() {
        return "AlptisConjoint{" +
                "dateNaissance='" + dateNaissance + '\'' +
                ", regimeObligatoire='" + regimeObligatoire + '\'' +
                ", categorieSocioprofessionnelle='" + categorieSocioprofessionnelle + '\'' +
                '}';
    }
}
