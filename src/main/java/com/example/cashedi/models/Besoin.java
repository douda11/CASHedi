package com.example.cashedi.models;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class Besoin {
    @NotBlank(message = "La date d'effet du nouveau contrat est requise")
    private String dateEffet; // Format ISO 8601: YYYY-MM-DD
}
