package com.example.cashedi.models.alptis;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class AlptisAdherent {

    @NotBlank(message = "Date naissance is required")
    @JsonProperty("date_naissance")
    private String dateNaissance;

    @NotBlank(message = "Regime obligatoire is required")
    @JsonProperty("regime_obligatoire")
    private String regimeObligatoire;

    @JsonProperty("code_postal")
    private String codePostal;

    @JsonProperty("cadre_exercice")
    private String cadreExercice;

    @JsonProperty("categorie_socioprofessionnelle")
    private String categorieSocioprofessionnelle;

    @JsonProperty("micro_entrepreneur")
    private boolean microEntrepreneur;

    // Constructors
    public AlptisAdherent() {}

    public AlptisAdherent(String dateNaissance, String regimeObligatoire, String codePostal, 
                         String cadreExercice, String categorieSocioprofessionnelle, boolean microEntrepreneur) {
        this.dateNaissance = dateNaissance;
        this.regimeObligatoire = regimeObligatoire;
        this.codePostal = codePostal;
        this.cadreExercice = cadreExercice;
        this.categorieSocioprofessionnelle = categorieSocioprofessionnelle;
        this.microEntrepreneur = microEntrepreneur;
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

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getCadreExercice() {
        return cadreExercice;
    }

    public void setCadreExercice(String cadreExercice) {
        this.cadreExercice = cadreExercice;
    }

    public String getCategorieSocioprofessionnelle() {
        return categorieSocioprofessionnelle;
    }

    public void setCategorieSocioprofessionnelle(String categorieSocioprofessionnelle) {
        this.categorieSocioprofessionnelle = categorieSocioprofessionnelle;
    }

    public boolean isMicroEntrepreneur() {
        return microEntrepreneur;
    }

    public void setMicroEntrepreneur(boolean microEntrepreneur) {
        this.microEntrepreneur = microEntrepreneur;
    }

    @Override
    public String toString() {
        return "AlptisAdherent{" +
                "dateNaissance='" + dateNaissance + '\'' +
                ", regimeObligatoire='" + regimeObligatoire + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", cadreExercice='" + cadreExercice + '\'' +
                ", categorieSocioprofessionnelle='" + categorieSocioprofessionnelle + '\'' +
                ", microEntrepreneur=" + microEntrepreneur +
                '}';
    }
}
