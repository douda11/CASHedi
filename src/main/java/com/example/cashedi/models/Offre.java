package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Offre {
    @NotBlank(message = "Le niveau de l'offre ne peut pas être vide")
    private String niveau;
    private boolean sur_complementaire;
}
