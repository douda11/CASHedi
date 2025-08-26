package com.example.cashedi.models.alptis.devis;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AlptisAdresse {

    private String numero;

    @JsonProperty("code_btq")
    private String codeBtq;

    @NotBlank(message = "Nature voie is required")
    @JsonProperty("nature_voie")
    private String natureVoie;

    @NotBlank(message = "Nom voie is required")
    @JsonProperty("nom_voie")
    private String nomVoie;

    private String complement;

    @NotBlank(message = "Code postal is required")
    @Pattern(regexp = "^\\d{5}$", message = "Code postal must be 5 digits")
    @JsonProperty("code_postal")
    private String codePostal;

    @NotBlank(message = "Ville is required")
    private String ville;

    @JsonProperty("adresse_complete")
    private String adresseComplete;

    @JsonProperty("is_adresse_complete")
    private boolean isAdresseComplete;

    // Constructors
    public AlptisAdresse() {}

    public AlptisAdresse(String numero, String codeBtq, String natureVoie, String nomVoie, 
                        String complement, String codePostal, String ville) {
        this.numero = numero;
        this.codeBtq = codeBtq;
        this.natureVoie = natureVoie;
        this.nomVoie = nomVoie;
        this.complement = complement;
        this.codePostal = codePostal;
        this.ville = ville;
        this.buildAdresseComplete();
    }

    // Method to build complete address
    private void buildAdresseComplete() {
        StringBuilder sb = new StringBuilder();
        if (numero != null && !numero.isEmpty()) sb.append(numero).append(" ");
        if (codeBtq != null && !codeBtq.isEmpty()) sb.append(codeBtq).append(" ");
        if (natureVoie != null && !natureVoie.isEmpty()) sb.append(natureVoie).append(" ");
        if (nomVoie != null && !nomVoie.isEmpty()) sb.append(nomVoie);
        
        this.adresseComplete = sb.toString().trim();
        this.isAdresseComplete = !this.adresseComplete.isEmpty();
    }

    // Getters and Setters
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
        buildAdresseComplete();
    }

    public String getCodeBtq() {
        return codeBtq;
    }

    public void setCodeBtq(String codeBtq) {
        this.codeBtq = codeBtq;
        buildAdresseComplete();
    }

    public String getNatureVoie() {
        return natureVoie;
    }

    public void setNatureVoie(String natureVoie) {
        this.natureVoie = natureVoie;
        buildAdresseComplete();
    }

    public String getNomVoie() {
        return nomVoie;
    }

    public void setNomVoie(String nomVoie) {
        this.nomVoie = nomVoie;
        buildAdresseComplete();
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getAdresseComplete() {
        return adresseComplete;
    }

    public void setAdresseComplete(String adresseComplete) {
        this.adresseComplete = adresseComplete;
    }

    public boolean isAdresseComplete() {
        return isAdresseComplete;
    }

    public void setAdresseComplete(boolean adresseComplete) {
        isAdresseComplete = adresseComplete;
    }

    @Override
    public String toString() {
        return "AlptisAdresse{" +
                "numero='" + numero + '\'' +
                ", codeBtq='" + codeBtq + '\'' +
                ", natureVoie='" + natureVoie + '\'' +
                ", nomVoie='" + nomVoie + '\'' +
                ", complement='" + complement + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", ville='" + ville + '\'' +
                ", adresseComplete='" + adresseComplete + '\'' +
                ", isAdresseComplete=" + isAdresseComplete +
                '}';
    }
}
