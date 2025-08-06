package com.example.cashedi.models.apivia.devis;



import lombok.Data;

@Data
public class Souscripteur {
    private String typeCivilite;
    private String nom;
    private String prenom;
    private String telephone;
    private String mobile;
    private String email;
    private Adresse adresse;
}
