package com.example.cashedi.models.alptis;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AlptisTarificationResponseComplete {

    @JsonProperty("resultats_tarification")
    private List<AlptisResultatTarification> resultatsTarification;

    @JsonProperty("code_retour")
    private String codeRetour;

    @JsonProperty("message")
    private String message;

    // Constructors
    public AlptisTarificationResponseComplete() {}

    public AlptisTarificationResponseComplete(List<AlptisResultatTarification> resultatsTarification, String codeRetour, String message) {
        this.resultatsTarification = resultatsTarification;
        this.codeRetour = codeRetour;
        this.message = message;
    }

    // Getters and Setters
    public List<AlptisResultatTarification> getResultatsTarification() {
        return resultatsTarification;
    }

    public void setResultatsTarification(List<AlptisResultatTarification> resultatsTarification) {
        this.resultatsTarification = resultatsTarification;
    }

    public String getCodeRetour() {
        return codeRetour;
    }

    public void setCodeRetour(String codeRetour) {
        this.codeRetour = codeRetour;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AlptisTarificationResponseComplete{" +
                "resultatsTarification=" + resultatsTarification +
                ", codeRetour='" + codeRetour + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
