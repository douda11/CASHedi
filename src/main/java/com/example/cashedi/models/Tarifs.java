package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarifs {
    private double totalMensuel;
    private double cotisationAssociation;
    private double cotisationMensuelleBase;
    private double cotisationMensuelleSurco;
    private double cotisationDeductibleMadelin;
    private double droitEntree;
}
