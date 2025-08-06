package com.example.cashedi.models;

import lombok.Data;

import java.util.List;

@Data
public class Proposition {
    private String codeProduit;
    private String libelleProduit;
    private String codeFormule;
    private String libelleFormule;
    private String codeTauxCommissionRetenu;
    private Double cotisationMensuelleEuros;
    private List<Frais> frais;
    private List<Document> documents;
}