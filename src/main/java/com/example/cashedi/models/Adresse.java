package com.example.cashedi.models;

import lombok.Data; 
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

@Data
public class Adresse {
    @NotBlank(message = "Le code postal ne peut pas être vide")
    @JsonProperty("code_postal")
    private String codePostal;
    @NotBlank(message = "La ville ne peut pas être vide")
    private String ville;
}
