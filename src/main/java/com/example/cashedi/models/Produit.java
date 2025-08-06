package com.example.cashedi.models;


public class Produit {
    private String codeProduit;
    private String codeFormule;
    private String TauxCommission;

    // Getters and Setters
    public String getCodeProduit() {
        return codeProduit;
    }

    public void setCodeProduit(String codeProduit) {
        this.codeProduit = codeProduit;
    }

    public String getCodeFormule() {
        return codeFormule;
    }

    public void setCodeFormule(String codeFormule) {
        this.codeFormule = codeFormule;
    }

    public String getTauxCommission() {
        return TauxCommission;
    }

    public void setTauxCommission(String TauxCommission) {
        this.TauxCommission = TauxCommission;
    }
}