package com.example.cashedi.models.apivia;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ApiviaTarificationRequest {

    @JsonProperty("action")
    private String action = "tarification";

    @JsonProperty("format")
    private String format = "json";

    @JsonProperty("produits")
    private String produits;

    @JsonProperty("formules")
    private String formules;

    @JsonProperty("renforts")
    private String renforts;

    @JsonProperty("options")
    private String options;

    @JsonProperty("codePostal")
    private String codePostal;

    @JsonProperty("dateEffet")
    private String dateEffet;

    @JsonProperty("dejaAssurance")
    private boolean dejaAssurance;

    @JsonProperty("isResiliationPourCompte")
    private boolean isResiliationPourCompte;

    @JsonProperty("typeAffaire")
    private String typeAffaire;

    @JsonProperty("typeObjectSortie")
    private String typeObjectSortie = "v2";

    @JsonProperty("beneficiaires")
    private List<ApiviaBeneficiaire> beneficiaires;
}
