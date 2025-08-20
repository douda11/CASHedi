package com.example.cashedi.models.apivia.devis;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class GeneratePdfDevisResponse {
    private String action;
    private String status;

    @JsonProperty("list")
    private Object list;
}