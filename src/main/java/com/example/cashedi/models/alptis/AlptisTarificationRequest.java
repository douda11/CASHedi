package com.example.cashedi.models.alptis;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class AlptisTarificationRequest {


    @NotBlank(message = "Date effet is required")
    @JsonProperty("date_effet")
    private String dateEffet;

    @NotNull(message = "Assures is required")
    private AlptisAssures assures;

    @NotNull(message = "Combinaisons is required")
    private List<AlptisCombinaisonRequest> combinaisons;

    // Constructors
    public AlptisTarificationRequest() {}

    public AlptisTarificationRequest(String codeDistributeur, String dateEffet, AlptisAssures assures, List<AlptisCombinaisonRequest> combinaisons) {
        this.dateEffet = dateEffet;
        this.assures = assures;
        this.combinaisons = combinaisons;
    }

    // Getters and Setters
 



    public String getDateEffet() {
        return dateEffet;
    }

    public void setDateEffet(String dateEffet) {
        this.dateEffet = dateEffet;
    }

    public AlptisAssures getAssures() {
        return assures;
    }

    public void setAssures(AlptisAssures assures) {
        this.assures = assures;
    }

    public List<AlptisCombinaisonRequest> getCombinaisons() {
        return combinaisons;
    }

    public void setCombinaisons(List<AlptisCombinaisonRequest> combinaisons) {
        this.combinaisons = combinaisons;
    }

    @Override
    public String toString() {
        return "AlptisTarificationRequest{" +
                ", dateEffet='" + dateEffet + '\'' +
                ", assures=" + assures +
                ", combinaisons=" + combinaisons +
                '}';
    }
}
