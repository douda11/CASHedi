package com.example.cashedi.models.alptis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlptisEnfant {

    @JsonProperty("date_naissance")
    private String dateNaissance;

    @JsonProperty("regime_obligatoire")
    private String regimeObligatoire;

    // Constructors
    public AlptisEnfant() {}

    public AlptisEnfant(String dateNaissance, String regimeObligatoire) {
        this.dateNaissance = dateNaissance;
        this.regimeObligatoire = regimeObligatoire;
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

    @Override
    public String toString() {
        return "AlptisEnfant{" +
                "dateNaissance='" + dateNaissance + '\'' +
                ", regimeObligatoire='" + regimeObligatoire + '\'' +
                '}';
    }
}
