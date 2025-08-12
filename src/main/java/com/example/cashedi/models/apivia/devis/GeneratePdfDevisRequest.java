package com.example.cashedi.models.apivia.devis;

import com.example.cashedi.models.apivia.ApiviaBeneficiaire;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GeneratePdfDevisRequest {
    @JsonProperty("action")
    private String action = "generate_pdf_devis";
    @JsonProperty("beneficiaires")
    private List<ApiviaBeneficiaire> beneficiaires;
    @JsonProperty("cle")
    private String cle;
    @JsonProperty("conseiller")
    private Conseiller conseiller;
    @JsonProperty("dateEffet")
    private String dateEffet;
    @JsonProperty("dejaAssurance")
    private boolean dejaAssurance;
    @JsonProperty("format")
    private String format = "json";
    @JsonProperty("formule")
    private String formule;
    @JsonProperty("frais")
    private String frais;
    @JsonProperty("isResiliationPourCompte")
    private boolean isResiliationPourCompte;
    @JsonProperty("options")
    private List<Object> options;
    @JsonProperty("produit")
    private String produit;
    @JsonProperty("renforts")
    private List<Object> renforts;
    @JsonProperty("souscripteur")
    private Souscripteur souscripteur;
    @JsonProperty("tarif")
    private float tarif;
}