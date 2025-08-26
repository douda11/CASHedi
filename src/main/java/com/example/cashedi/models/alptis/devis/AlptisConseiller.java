package com.example.cashedi.models.alptis.devis;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class AlptisConseiller {

    @NotBlank(message = "Nom is required")
    private String nom;

    @NotBlank(message = "Prenom is required")
    private String prenom;

    @JsonProperty("type_conseiller")
    private String typeConseiller;

    @NotBlank(message = "ORIAS is required")
    private String orias;

    private String telephone;

    private String email;

    // Constructors
    public AlptisConseiller() {}

    public AlptisConseiller(String nom, String prenom, String typeConseiller, String orias, String telephone, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.typeConseiller = typeConseiller;
        this.orias = orias;
        this.telephone = telephone;
        this.email = email;
    }

    // Getters and Setters
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

    public String getTypeConseiller() {
        return typeConseiller;
    }

    public void setTypeConseiller(String typeConseiller) {
        this.typeConseiller = typeConseiller;
    }

    public String getOrias() {
        return orias;
    }

    public void setOrias(String orias) {
        this.orias = orias;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "AlptisConseiller{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", typeConseiller='" + typeConseiller + '\'' +
                ", orias='" + orias + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
