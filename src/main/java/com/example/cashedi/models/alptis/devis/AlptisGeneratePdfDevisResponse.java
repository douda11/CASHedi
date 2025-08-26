package com.example.cashedi.models.alptis.devis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlptisGeneratePdfDevisResponse {

    @JsonProperty("code_retour")
    private String codeRetour;

    private String message;

    @JsonProperty("fichier_pdf")
    private String fichierPdf; // Base64 encoded PDF content

    @JsonProperty("nom_fichier")
    private String nomFichier;

    @JsonProperty("taille_fichier")
    private Long tailleFichier;

    // Constructors
    public AlptisGeneratePdfDevisResponse() {}

    public AlptisGeneratePdfDevisResponse(String codeRetour, String message, String fichierPdf, String nomFichier, Long tailleFichier) {
        this.codeRetour = codeRetour;
        this.message = message;
        this.fichierPdf = fichierPdf;
        this.nomFichier = nomFichier;
        this.tailleFichier = tailleFichier;
    }

    // Getters and Setters
    public String getCodeRetour() {
        return codeRetour;
    }

    public void setCodeRetour(String codeRetour) {
        this.codeRetour = codeRetour;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFichierPdf() {
        return fichierPdf;
    }

    public void setFichierPdf(String fichierPdf) {
        this.fichierPdf = fichierPdf;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public Long getTailleFichier() {
        return tailleFichier;
    }

    public void setTailleFichier(Long tailleFichier) {
        this.tailleFichier = tailleFichier;
    }

    @Override
    public String toString() {
        return "AlptisGeneratePdfDevisResponse{" +
                "codeRetour='" + codeRetour + '\'' +
                ", message='" + message + '\'' +
                ", nomFichier='" + nomFichier + '\'' +
                ", tailleFichier=" + tailleFichier +
                '}';
    }
}
