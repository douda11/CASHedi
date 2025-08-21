package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlptisTarificationResponse {
    private Tarifs tarifs;
    private String typeCotisation;
    private String generationTarif;
    private String codeAssociation;
    private int millesime;
}
