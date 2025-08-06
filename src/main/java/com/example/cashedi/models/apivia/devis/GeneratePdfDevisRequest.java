package com.example.cashedi.models.apivia.devis;

import com.example.cashedi.models.apivia.ApiviaBeneficiaire;
import lombok.Data;

import java.util.List;

@Data
public class GeneratePdfDevisRequest {
    private String action = "generate_pdf_devis";
    private List<ApiviaBeneficiaire> beneficiaires;
    private String cle;
    private Conseiller conseiller;
    private String dateEffet;
    private boolean dejaAssurance;
    private String format = "json";
    private String formule;
    private String frais;
    private boolean isResiliationPourCompte;
    private List<Object> options;
    private String produit;
    private List<Object> renforts;
    private Souscripteur souscripteur;
    private float tarif;
}
