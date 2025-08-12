package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {
    private String code_mode;
    private String code_frequence;
    private String iban;
    private String bic;
    private Payeur payeur;
}
