package com.example.cashedi.models.alptis.devis;

import com.example.cashedi.models.alptis.AlptisAdherent;
import com.example.cashedi.models.alptis.AlptisConjoint;
import com.example.cashedi.models.alptis.AlptisEnfant;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class AlptisGeneratePdfDevisRequest {

    @NotBlank(message = "Code distributeur is required")
    @JsonProperty("code_distributeur")
    private String codeDistributeur;

    @NotBlank(message = "Date effet is required")
    @JsonProperty("date_effet")
    private String dateEffet;

    @NotBlank(message = "Niveau offre is required")
    @JsonProperty("niveau_offre")
    private String niveauOffre;

    @JsonProperty("sur_complementaire")
    private boolean surComplementaire;

    @NotBlank(message = "Commissionnement is required")
    private String commissionnement;

    @JsonProperty("type_cotisation")
    private String typeCotisation;

    @NotNull(message = "Souscripteur is required")
    private AlptisSouscripteur souscripteur;

    @NotNull(message = "Conseiller is required")
    private AlptisConseiller conseiller;

    @NotNull(message = "Adherent is required")
    private AlptisAdherent adherent;

    private AlptisConjoint conjoint;

    private List<AlptisEnfant> enfants;

    @JsonProperty("tarif_mensuel")
    private Double tarifMensuel;

    @JsonProperty("droit_entree")
    private Double droitEntree;

    // Constructors
    public AlptisGeneratePdfDevisRequest() {}

    // Getters and Setters
    public String getCodeDistributeur() {
        return codeDistributeur;
    }

    public void setCodeDistributeur(String codeDistributeur) {
        this.codeDistributeur = codeDistributeur;
    }

    public String getDateEffet() {
        return dateEffet;
    }

    public void setDateEffet(String dateEffet) {
        this.dateEffet = dateEffet;
    }

    public String getNiveauOffre() {
        return niveauOffre;
    }

    public void setNiveauOffre(String niveauOffre) {
        this.niveauOffre = niveauOffre;
    }

    public boolean isSurComplementaire() {
        return surComplementaire;
    }

    public void setSurComplementaire(boolean surComplementaire) {
        this.surComplementaire = surComplementaire;
    }

    public String getCommissionnement() {
        return commissionnement;
    }

    public void setCommissionnement(String commissionnement) {
        this.commissionnement = commissionnement;
    }

    public String getTypeCotisation() {
        return typeCotisation;
    }

    public void setTypeCotisation(String typeCotisation) {
        this.typeCotisation = typeCotisation;
    }

    public AlptisSouscripteur getSouscripteur() {
        return souscripteur;
    }

    public void setSouscripteur(AlptisSouscripteur souscripteur) {
        this.souscripteur = souscripteur;
    }

    public AlptisConseiller getConseiller() {
        return conseiller;
    }

    public void setConseiller(AlptisConseiller conseiller) {
        this.conseiller = conseiller;
    }

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

    public Double getTarifMensuel() {
        return tarifMensuel;
    }

    public void setTarifMensuel(Double tarifMensuel) {
        this.tarifMensuel = tarifMensuel;
    }

    public Double getDroitEntree() {
        return droitEntree;
    }

    public void setDroitEntree(Double droitEntree) {
        this.droitEntree = droitEntree;
    }

    @Override
    public String toString() {
        return "AlptisGeneratePdfDevisRequest{" +
                "codeDistributeur='" + codeDistributeur + '\'' +
                ", dateEffet='" + dateEffet + '\'' +
                ", niveauOffre='" + niveauOffre + '\'' +
                ", surComplementaire=" + surComplementaire +
                ", commissionnement='" + commissionnement + '\'' +
                ", typeCotisation='" + typeCotisation + '\'' +
                ", souscripteur=" + souscripteur +
                ", conseiller=" + conseiller +
                ", adherent=" + adherent +
                ", conjoint=" + conjoint +
                ", enfants=" + enfants +
                ", tarifMensuel=" + tarifMensuel +
                ", droitEntree=" + droitEntree +
                '}';
    }
}
