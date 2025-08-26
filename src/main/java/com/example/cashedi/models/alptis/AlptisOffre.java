package com.example.cashedi.models.alptis;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class AlptisOffre {

    @NotBlank(message = "Niveau is required")
    private String niveau;

    @JsonProperty("sur_complementaire")
    private boolean surComplementaire;

    // Constructors
    public AlptisOffre() {}

    public AlptisOffre(String niveau, boolean surComplementaire) {
        this.niveau = niveau;
        this.surComplementaire = surComplementaire;
    }

    // Getters and Setters
    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public boolean isSurComplementaire() {
        return surComplementaire;
    }

    public void setSurComplementaire(boolean surComplementaire) {
        this.surComplementaire = surComplementaire;
    }

    @Override
    public String toString() {
        return "AlptisOffre{" +
                "niveau='" + niveau + '\'' +
                ", surComplementaire=" + surComplementaire +
                '}';
    }
}
