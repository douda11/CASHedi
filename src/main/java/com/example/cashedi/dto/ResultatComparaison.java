package com.example.cashedi.dto;

import com.example.cashedi.entites.Formule;

public class ResultatComparaison {
    private String nomDeLOffre;
    private String nomFormule; // Utiliser un nom de formule textuel
    private double score;
    private Formule details;
    private Double tarifMensuel;

    // Constructeur pour les formules de la DB
    public ResultatComparaison(String nomDeLOffre, String nomFormule, double score, Formule details) {
        this.nomDeLOffre = nomDeLOffre;
        this.nomFormule = nomFormule;
        this.score = score;
        this.details = details;
    }

    // Constructeur pour les offres externes (Utwin)
    public ResultatComparaison(String nomDeLOffre, String nomFormule, Double tarif) {
        this.nomDeLOffre = nomDeLOffre;
        this.nomFormule = nomFormule;
        this.tarifMensuel = tarif;
        this.score = tarif; // Utiliser le tarif comme score initial
        this.details = null; // Pas de détails de garanties pour les offres externes
    }
    public Double getTarifMensuel() {
        return tarifMensuel;
    }

    public void setTarifMensuel(Double tarifMensuel) {
        this.tarifMensuel = tarifMensuel;
    }
    // Getters et Setters
    public String getNomDeLOffre() {
        return nomDeLOffre;
    }

    public void setNomDeLOffre(String nomDeLOffre) {
        this.nomDeLOffre = nomDeLOffre;
    }

    public String getNomFormule() {
        return nomFormule;
    }

    public void setNomFormule(String nomFormule) {
        this.nomFormule = nomFormule;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Formule getDetails() {
        return details;
    }

    public void setDetails(Formule details) {
        this.details = details;
    }

    // Méthode toString pour afficher l'objet sous forme de chaîne de caractères
    @Override
    public String toString() {
        return "ResultatComparaison{" +
                "nomDeLOffre='" + nomDeLOffre + '\'' +
                ", nomFormule='" + nomFormule + '\'' +
                ", score=" + score +
                ", tarifMensuel=" + tarifMensuel +
                ", details=" + details +
                '}';
    }
}

