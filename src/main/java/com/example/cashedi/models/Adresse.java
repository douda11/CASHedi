package com.example.cashedi.models;

import lombok.Data; 

import jakarta.validation.constraints.NotBlank;

@Data
public class Adresse {
    @NotBlank(message = "Le code postal ne peut pas être vide")
    private String codePostal;
    @NotBlank(message = "La ville ne peut pas être vide")
    private String ville;
}
