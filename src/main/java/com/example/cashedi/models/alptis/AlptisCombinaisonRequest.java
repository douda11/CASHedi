package com.example.cashedi.models.alptis;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class AlptisCombinaisonRequest {

    @NotNull(message = "Numero is required")
    private Integer numero;

    @NotNull(message = "Offre is required")
    private AlptisOffre offre;

    @NotNull(message = "Commissionnement is required")
    private String commissionnement;
    
    @NotNull(message = "Ayants droit is required")
    private Map<String, Object> ayants_droit;

    // Constructors
    public AlptisCombinaisonRequest() {}

    public AlptisCombinaisonRequest(Integer numero, AlptisOffre offre, String commissionnement) {
        this.numero = numero;
        this.offre = offre;
        this.commissionnement = commissionnement;
    }

    // Getters and Setters
    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public AlptisOffre getOffre() {
        return offre;
    }

    public void setOffre(AlptisOffre offre) {
        this.offre = offre;
    }

    public String getCommissionnement() {
        return commissionnement;
    }

    public void setCommissionnement(String commissionnement) {
        this.commissionnement = commissionnement;
    }

    public Map<String, Object> getAyants_droit() {
        return ayants_droit;
    }

    public void setAyants_droit(Map<String, Object> ayants_droit) {
        this.ayants_droit = ayants_droit;
    }

    @Override
    public String toString() {
        return "AlptisCombinaisonRequest{" +
                "numero=" + numero +
                ", offre=" + offre +
                ", commissionnement='" + commissionnement + '\'' +
                ", ayants_droit=" + ayants_droit +
                '}';
    }
}
