package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assures {
    private Adherent adherent;
    private Conjoint conjoint;
    private List<Enfant> enfants;
}
