package com.example.cashedi.models.apivia.devis;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Adresse {
        @JsonProperty("numero")
    private String numero;
        @JsonProperty("codeBtq")
    private String codeBtq;
        @JsonProperty("natureVoie")
    private String natureVoie;
        @JsonProperty("nomVoie")
    private String nomVoie;
        @JsonProperty("complement")
    private String complement;
        @JsonProperty("codePostal")
    private String codePostal;
        @JsonProperty("ville")
    private String ville;
        @JsonProperty("adresseComplete")
    private String adresseComplete;
    @JsonProperty("isAdresseComplete")
    private boolean adresseEstComplete;
}
