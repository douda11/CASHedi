package com.example.cashedi.entites;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "plans_sante")
public class Formule {

    @Id
    private String id;

    @Field("Formule")
    private String formule;

    @Field("Hospitalisation")
    private int hospitalisation;

    @Field("Honoraires")
    private int honoraires;

    @Field("Chambre Particulière")
    private int chambreParticuliere;

    @Field("Dentaire")
    private int dentaire;

    @Field("Orthodontie")
    private int orthodontie;

    @Field("Forfait Dentaire")
    private int forfaitDentaire;

    @Field("Forfait Optique")
    private int forfaitOptique;

    @Field("nom_de_l_offre")
    private String nomDeLOffre;

    // Constructeurs, Getters et Setters
    public Formule() {
    }

    public Formule(String id, String formule, int hospitalisation, int honoraires, int chambreParticuliere,
                   int dentaire, int orthodontie, int forfaitDentaire, int forfaitOptique, String nomDeLOffre) {
        this.id = id;
        this.formule = formule;
        this.hospitalisation = hospitalisation;
        this.honoraires = honoraires;
        this.chambreParticuliere = chambreParticuliere;
        this.dentaire = dentaire;
        this.orthodontie = orthodontie;
        this.forfaitDentaire = forfaitDentaire;
        this.forfaitOptique = forfaitOptique;
        this.nomDeLOffre = nomDeLOffre;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormule() {
        return formule;
    }

    public void setFormule(String formule) {
        this.formule = formule;
    }

    public int getHospitalisation() {
        return hospitalisation;
    }

    public void setHospitalisation(int hospitalisation) {
        this.hospitalisation = hospitalisation;
    }

    public int getHonoraires() {
        return honoraires;
    }

    public void setHonoraires(int honoraires) {
        this.honoraires = honoraires;
    }

    public int getChambreParticuliere() {
        return chambreParticuliere;
    }

    public void setChambreParticuliere(int chambreParticuliere) {
        this.chambreParticuliere = chambreParticuliere;
    }

    public int getDentaire() {
        return dentaire;
    }

    public void setDentaire(int dentaire) {
        this.dentaire = dentaire;
    }

    public int getOrthodontie() {
        return orthodontie;
    }

    public void setOrthodontie(int orthodontie) {
        this.orthodontie = orthodontie;
    }

    public int getForfaitDentaire() {
        return forfaitDentaire;
    }

    public void setForfaitDentaire(int forfaitDentaire) {
        this.forfaitDentaire = forfaitDentaire;
    }

    public int getForfaitOptique() {
        return forfaitOptique;
    }

    public void setForfaitOptique(int forfaitOptique) {
        this.forfaitOptique = forfaitOptique;
    }

    public String getNomDeLOffre() {
        return nomDeLOffre;
    }

    public void setNomDeLOffre(String nomDeLOffre) {
        this.nomDeLOffre = nomDeLOffre;
    }

    @Override
    public String toString() {
        return "Formule{" +
                "id='" + id + '\'' +
                ", formule=" + formule +
                ", hospitalisation=" + hospitalisation +
                ", honoraires=" + honoraires +
                ", chambreParticuliere=" + chambreParticuliere +
                ", dentaire=" + dentaire +
                ", orthodontie=" + orthodontie +
                ", forfaitDentaire=" + forfaitDentaire +
                ", forfaitOptique=" + forfaitOptique +
                ", nomDeLOffre='" + nomDeLOffre + '\'' +
                '}';
    }
}