package com.example.cashedi.entites;

public class Clientt {
    private String title; // Titre (Monsieur, Madame, etc.)
    private String nationality; // Nationalité
    private String birthCountry; // Pays de naissance
    private String city; // Ville
    private String postCode; // Code postal
    private String birthDate; // Date de naissance

    // Constructeur, getters, et setters
    public Clientt() {}

    public Clientt(String title, String nationality, String birthCountry, String city, String postCode, String birthDate) {
        this.title = title;
        this.nationality = nationality;
        this.birthCountry = birthCountry;
        this.city = city;
        this.postCode = postCode;
        this.birthDate = birthDate;
    }

    // Getters et Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBirthCountry() {
        return birthCountry;
    }

    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
