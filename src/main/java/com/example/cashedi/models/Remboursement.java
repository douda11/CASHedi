package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Remboursement {
    private String iban;
    private String bic;
    private boolean identique_a_compte_paiement;
}
