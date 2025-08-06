package com.example.cashedi.entites;

import java.util.List;

public class BesoinClient {
    private int hospitalisation;
    private int dentaire;
    private int forfaitOptique;
    private int honoraires;
    private int chambreParticuliere;
    private int orthodontie;
        private int forfaitDentaire;

    private String codePostal;
    private String dateEffet;
    private List<AssureClient> assures;

    // Constructeur par défaut
    public BesoinClient() {
    }

    // Constructeur avec tous les champs
    public BesoinClient(int hospitalisation, int dentaire, int forfaitOptique, int honoraires,
                        int chambreParticuliere, int orthodontie, int forfaitDentaire) {
        this.hospitalisation = hospitalisation;
        this.dentaire = dentaire;
        this.forfaitOptique = forfaitOptique;
        this.honoraires = honoraires;
        this.chambreParticuliere = chambreParticuliere;
        this.orthodontie = orthodontie;
        this.forfaitDentaire = forfaitDentaire;
    }

    // Getters et Setters
    public int getHospitalisation() {
        return hospitalisation;
    }

    public void setHospitalisation(int hospitalisation) {
        this.hospitalisation = hospitalisation;
    }

    public int getDentaire() {
        return dentaire;
    }

    public void setDentaire(int dentaire) {
        this.dentaire = dentaire;
    }

    public int getForfaitOptique() {
        return forfaitOptique;
    }

    public void setForfaitOptique(int forfaitOptique) {
        this.forfaitOptique = forfaitOptique;
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

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getDateEffet() {
        return dateEffet;
    }

    public void setDateEffet(String dateEffet) {
        this.dateEffet = dateEffet;
    }

    public List<AssureClient> getAssures() {
        return assures;
    }

    public void setAssures(List<AssureClient> assures) {
        this.assures = assures;
    }

    @Override
    public String toString() {
        return "BesoinClient{" +
                "hospitalisation=" + hospitalisation +
                ", dentaire=" + dentaire +
                ", forfaitOptique=" + forfaitOptique +
                ", honoraires=" + honoraires +
                ", chambreParticuliere=" + chambreParticuliere +
                ", orthodontie=" + orthodontie +
                ", forfaitDentaire=" + forfaitDentaire +
                ", codePostal='" + codePostal + '\'' +
                ", dateEffet='" + dateEffet + '\'' +
                ", assures=" + assures +
                '}';
    }
}