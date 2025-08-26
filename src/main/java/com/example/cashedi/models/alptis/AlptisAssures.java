package com.example.cashedi.models.alptis;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlptisAssures {

    private AlptisAdherent adherent;
    private AlptisConjoint conjoint;
    private List<AlptisEnfant> enfants;

    // Constructors
    public AlptisAssures() {}

    public AlptisAssures(AlptisAdherent adherent, AlptisConjoint conjoint, List<AlptisEnfant> enfants) {
        this.adherent = adherent;
        this.conjoint = conjoint;
        this.enfants = enfants;
    }

    // Getters and Setters
    public AlptisAdherent getAdherent() {
        return adherent;
    }

    public void setAdherent(AlptisAdherent adherent) {
        this.adherent = adherent;
    }

    public AlptisConjoint getConjoint() {
        return conjoint;
    }

    public void setConjoint(AlptisConjoint conjoint) {
        this.conjoint = conjoint;
    }

    public List<AlptisEnfant> getEnfants() {
        return enfants;
    }

    public void setEnfants(List<AlptisEnfant> enfants) {
        this.enfants = enfants;
    }

    @Override
    public String toString() {
        return "AlptisAssures{" +
                "adherent=" + adherent +
                ", conjoint=" + conjoint +
                ", enfants=" + enfants +
                '}';
    }
}
