package com.example.cashedi.models.apivia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import com.example.cashedi.models.apivia.devis.Adresse;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiviaBeneficiaire {

    @JsonProperty("typeBeneficiaire")
    private String typeBeneficiaire;

    @JsonProperty("dateDeNaissance")
    private String dateDeNaissance;

    @JsonProperty("typeRegime")
    private String typeRegime;

    @JsonProperty("regimeSocial")
    private String regimeSocial;

    @JsonProperty("typeAutreBeneficiaire")
    private String typeAutreBeneficiaire;
    private Adresse adresse; // Optional
}
