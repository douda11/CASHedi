package com.example.cashedi.models;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Data
public class Souscripteur {
    @Valid
    @NotNull(message = "L'adresse du souscripteur est requise")
    private Adresse adresse;
}