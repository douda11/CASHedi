package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Combinaison {
    private int numero;
    private Offre offre;
    private String commissionnement;
    @JsonProperty("ayants_droit")
    private AyantsDroit ayantsDroit;
}
