package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordonnees {
    private Adresse adresse;
    private String telephone_fixe;
    private String telephone_portable;
    private String email;
}
