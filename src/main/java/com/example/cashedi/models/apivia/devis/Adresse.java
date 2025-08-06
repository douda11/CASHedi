package com.example.cashedi.models.apivia.devis;

import lombok.Data;

@Data
public class Adresse {
    private String numero;
    private String codeBtq;
    private String natureVoie;
    private String nomVoie;
    private String complement;
    private String codePostal;
    private String ville;
    private boolean isAdresseComplete;
}
