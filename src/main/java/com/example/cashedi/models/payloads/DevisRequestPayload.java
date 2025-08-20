package com.example.cashedi.models.payloads;

import com.example.cashedi.models.apivia.devis.GeneratePdfDevisRequest;

public class DevisRequestPayload {
    private GeneratePdfDevisRequest devisRequest;
    private Float tarif;

    public GeneratePdfDevisRequest getDevisRequest() {
        return devisRequest;
    }

    public void setDevisRequest(GeneratePdfDevisRequest devisRequest) {
        this.devisRequest = devisRequest;
    }

    public Float getTarif() {
        return tarif;
    }

    public void setTarif(Float tarif) {
        this.tarif = tarif;
    }
}
