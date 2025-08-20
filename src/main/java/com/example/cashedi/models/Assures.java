package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assures {
    @NotNull(message = "L'adhérent ne peut pas être nul")
    @Valid
    private Adherent adherent;
    private Conjoint conjoint;
    @Valid
    private List<Enfant> enfants;
}
