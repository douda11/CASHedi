package com.example.cashedi.models;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class TarificationRequestUtwin {
    @Valid
    @NotNull(message = "Le souscripteur est requis")
    private Souscripteur souscripteur;
    
    @Valid
    @NotNull(message = "Le besoin est requis")
    private Besoin besoin;
    
    @Valid
    @NotEmpty(message = "Au moins un assuré est requis")
    private List<Assure> assures;
    
    // Méthode pour valider qu'il existe un assuré principal
    public boolean hasAssurePrincipal() {
        if (assures == null || assures.isEmpty()) {
            return false;
        }
        return assures.stream().anyMatch(assure -> "AssurePrincipal".equals(assure.getCodeTypeRole()));
    }
}