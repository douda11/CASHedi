package com.example.cashedi.models.apivia.devis;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Conseiller {
        @JsonProperty("nom")
    private String nom;
        @JsonProperty("prenom")
    private String prenom;
        @JsonProperty("typeConseiller")
    private String typeConseiller;
        @JsonProperty("orias")
    private String orias;
}
