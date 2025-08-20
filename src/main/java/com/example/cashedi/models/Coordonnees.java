package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordonnees {
    @NotNull(message = "L'adresse ne peut pas être nulle")
    @Valid
    private Adresse adresse;
    private String telephone_fixe;
    @NotBlank(message = "Le téléphone portable ne peut pas être vide")
    private String telephone_portable;
    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "L'email doit être valide")
    private String email;
}
