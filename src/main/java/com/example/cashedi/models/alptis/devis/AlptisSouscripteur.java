package com.example.cashedi.models.alptis.devis;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public class AlptisSouscripteur {

    @NotBlank(message = "Type civilite is required")
    @JsonProperty("type_civilite")
    private String typeCivilite;

    @NotBlank(message = "Nom is required")
    private String nom;

    @NotBlank(message = "Prenom is required")
    private String prenom;

    @JsonProperty("date_naissance")
    private String dateNaissance;

    private String telephone;

    private String mobile;

    @Email(message = "Email should be valid")
    private String email;

    private AlptisAdresse adresse;

    // Constructors
    public AlptisSouscripteur() {}

    public AlptisSouscripteur(String typeCivilite, String nom, String prenom, String dateNaissance, 
                             String telephone, String mobile, String email, AlptisAdresse adresse) {
        this.typeCivilite = typeCivilite;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
        this.mobile = mobile;
        this.email = email;
        this.adresse = adresse;
    }

    // Getters and Setters
    public String getTypeCivilite() {
        return typeCivilite;
    }

    public void setTypeCivilite(String typeCivilite) {
        this.typeCivilite = typeCivilite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AlptisAdresse getAdresse() {
        return adresse;
    }

    public void setAdresse(AlptisAdresse adresse) {
        this.adresse = adresse;
    }

    @Override
    public String toString() {
        return "AlptisSouscripteur{" +
                "typeCivilite='" + typeCivilite + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaissance='" + dateNaissance + '\'' +
                ", telephone='" + telephone + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", adresse=" + adresse +
                '}';
    }
}
