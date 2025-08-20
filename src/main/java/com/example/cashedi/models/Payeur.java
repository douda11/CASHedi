package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payeur {
    private String type;
    private boolean adherent_habilite_signature;
    private String denomination_sociale;
    private Adresse adresse;
}
