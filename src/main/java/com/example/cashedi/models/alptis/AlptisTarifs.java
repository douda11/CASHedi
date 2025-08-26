package com.example.cashedi.models.alptis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlptisTarifs {

    @JsonProperty("total_mensuel")
    private Double totalMensuel;

    @JsonProperty("cotisation_mensuelle_base")
    private Double cotisationMensuelleBase;

    @JsonProperty("cotisation_mensuelle_surco")
    private Double cotisationMensuelleSurco;

    @JsonProperty("cotisation_association")
    private Double cotisationAssociation;

    @JsonProperty("cotisation_deductible_madelin")
    private Double cotisationDeductibleMadelin;

    @JsonProperty("droit_entree")
    private Double droitEntree;

    // Constructors
    public AlptisTarifs() {}

    public AlptisTarifs(Double totalMensuel, Double cotisationMensuelleBase, Double cotisationMensuelleSurco, 
                       Double cotisationAssociation, Double cotisationDeductibleMadelin, Double droitEntree) {
        this.totalMensuel = totalMensuel;
        this.cotisationMensuelleBase = cotisationMensuelleBase;
        this.cotisationMensuelleSurco = cotisationMensuelleSurco;
        this.cotisationAssociation = cotisationAssociation;
        this.cotisationDeductibleMadelin = cotisationDeductibleMadelin;
        this.droitEntree = droitEntree;
    }

    // Getters and Setters
    public Double getTotalMensuel() {
        return totalMensuel;
    }

    public void setTotalMensuel(Double totalMensuel) {
        this.totalMensuel = totalMensuel;
    }

    public Double getCotisationMensuelleBase() {
        return cotisationMensuelleBase;
    }

    public void setCotisationMensuelleBase(Double cotisationMensuelleBase) {
        this.cotisationMensuelleBase = cotisationMensuelleBase;
    }

    public Double getCotisationMensuelleSurco() {
        return cotisationMensuelleSurco;
    }

    public void setCotisationMensuelleSurco(Double cotisationMensuelleSurco) {
        this.cotisationMensuelleSurco = cotisationMensuelleSurco;
    }

    public Double getCotisationAssociation() {
        return cotisationAssociation;
    }

    public void setCotisationAssociation(Double cotisationAssociation) {
        this.cotisationAssociation = cotisationAssociation;
    }

    public Double getCotisationDeductibleMadelin() {
        return cotisationDeductibleMadelin;
    }

    public void setCotisationDeductibleMadelin(Double cotisationDeductibleMadelin) {
        this.cotisationDeductibleMadelin = cotisationDeductibleMadelin;
    }

    public Double getDroitEntree() {
        return droitEntree;
    }

    public void setDroitEntree(Double droitEntree) {
        this.droitEntree = droitEntree;
    }

    @Override
    public String toString() {
        return "AlptisTarifs{" +
                "totalMensuel=" + totalMensuel +
                ", cotisationMensuelleBase=" + cotisationMensuelleBase +
                ", cotisationMensuelleSurco=" + cotisationMensuelleSurco +
                ", cotisationAssociation=" + cotisationAssociation +
                ", cotisationDeductibleMadelin=" + cotisationDeductibleMadelin +
                ", droitEntree=" + droitEntree +
                '}';
    }
}
