package com.example.cashedi.models;

import lombok.Data; // Importez Lombok Data pour tous les getters, setters, constructeur sans arg, etc.

import java.util.List;

@Data // Remplace @Getter et ajoute @Setter, @NoArgsConstructor, @EqualsAndHashCode, @ToString
public class TarificationRequestUtwin {
    private Souscripteur souscripteur;
    private Besoin besoin;
    private List<Assure> assures;
}