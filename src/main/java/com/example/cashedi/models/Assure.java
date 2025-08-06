package com.example.cashedi.models;

import lombok.Data;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
public class Assure {
    private String codeRegimeObligatoire;
    private String codeTypeRole;
    private String dateDeNaissance;

    // Méthode pour calculer l'âge à partir de la date de naissance

}