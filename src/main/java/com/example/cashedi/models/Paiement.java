package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {
    @NotBlank(message = "Le code mode de paiement ne peut pas être vide")
    private String code_mode;
    @NotBlank(message = "Le code fréquence de paiement ne peut pas être vide")
    private String code_frequence;
    @NotBlank(message = "L'IBAN ne peut pas être vide")
    private String iban;
    private String bic;
    @NotNull(message = "Le payeur ne peut pas être nul")
    @Valid
    private Payeur payeur;
}
