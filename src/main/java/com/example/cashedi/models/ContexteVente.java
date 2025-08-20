package com.example.cashedi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContexteVente {
    @JsonProperty("vente_presentiel")
    private boolean ventePresentiel;
    @JsonProperty("appel_par_courtier")
    private boolean appelParCourtier;
    @JsonProperty("consentement_client_appel")
    private boolean consentementClientAppel;
    @JsonProperty("accepte_reglementation")
    private boolean accepteReglementation;
}
