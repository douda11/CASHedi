package com.example.cashedi.models.alptis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlptisResultatTarification {

    private AlptisTarifs tarifs;

    @JsonProperty("generation_tarif")
    private String generationTarif;

    @JsonProperty("code_association")
    private String codeAssociation;

    @JsonProperty("type_cotisation")
    private String typeCotisation;

    private Integer millesime;

    // Constructors
    public AlptisResultatTarification() {}

    public AlptisResultatTarification(AlptisTarifs tarifs, String generationTarif, String codeAssociation, String typeCotisation, Integer millesime) {
        this.tarifs = tarifs;
        this.generationTarif = generationTarif;
        this.codeAssociation = codeAssociation;
        this.typeCotisation = typeCotisation;
        this.millesime = millesime;
    }

    // Getters and Setters
    public AlptisTarifs getTarifs() {
        return tarifs;
    }

    public void setTarifs(AlptisTarifs tarifs) {
        this.tarifs = tarifs;
    }

    public String getGenerationTarif() {
        return generationTarif;
    }

    public void setGenerationTarif(String generationTarif) {
        this.generationTarif = generationTarif;
    }

    public String getCodeAssociation() {
        return codeAssociation;
    }

    public void setCodeAssociation(String codeAssociation) {
        this.codeAssociation = codeAssociation;
    }

    public String getTypeCotisation() {
        return typeCotisation;
    }

    public void setTypeCotisation(String typeCotisation) {
        this.typeCotisation = typeCotisation;
    }

    public Integer getMillesime() {
        return millesime;
    }

    public void setMillesime(Integer millesime) {
        this.millesime = millesime;
    }

    @Override
    public String toString() {
        return "AlptisResultatTarification{" +
                "tarifs=" + tarifs +
                ", generationTarif='" + generationTarif + '\'' +
                ", codeAssociation='" + codeAssociation + '\'' +
                ", typeCotisation='" + typeCotisation + '\'' +
                ", millesime=" + millesime +
                '}';
    }
}
