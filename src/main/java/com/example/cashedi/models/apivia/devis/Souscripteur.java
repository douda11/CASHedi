package com.example.cashedi.models.apivia.devis;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Souscripteur {
        @JsonProperty("typeCivilite")
    private String typeCivilite;
        @JsonProperty("nom")
    private String nom;
        @JsonProperty("prenom")
    private String prenom;
        @JsonProperty("telephone")
    private String telephone;
        @JsonProperty("mobile")
    private String mobile;
        @JsonProperty("email")
    private String email;
        @JsonProperty("adresse")
    private Adresse adresse;
}
