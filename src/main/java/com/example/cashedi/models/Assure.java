package com.example.cashedi.models;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
public class Assure {
    @NotBlank(message = "Le code régime obligatoire est requis")
    private String codeRegimeObligatoire;
    
    @NotBlank(message = "Le rôle de l'assuré est requis")
    private String codeTypeRole;
    
    @NotBlank(message = "La date de naissance est requise")
    private String dateDeNaissance;

    // Méthode pour calculer l'âge à partir de la date de naissance
    public int calculerAge() {
        if (dateDeNaissance == null || dateDeNaissance.isEmpty()) {
            return 0;
        }
        try {
            LocalDate birthDate = LocalDate.parse(dateDeNaissance, DateTimeFormatter.ISO_LOCAL_DATE);
            return LocalDate.now().getYear() - birthDate.getYear();
        } catch (DateTimeParseException e) {
            return 0;
        }
    }
}