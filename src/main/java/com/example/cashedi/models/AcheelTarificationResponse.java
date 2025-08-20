package com.example.cashedi.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class AcheelTarificationResponse {

    @JsonProperty("id_global_tarif")
    private String idGlobalTarif;

    private Map<String, Formula> formulas = new HashMap<>();

    @JsonAnySetter
    public void addFormula(String key, Formula value) {
        if (key.startsWith("formula_")) {
            formulas.put(key, value);
        }
    }

    @Data
    public static class Formula {
        @JsonProperty("ResponseAttribute")
        private List<ResponseAttribute> responseAttributes;
    }

    @Data
    public static class ResponseAttribute {
        @JsonProperty("ResponseAttributeName")
        private String responseAttributeName;

        @JsonProperty("ResponseAttributeValue")
        private Object responseAttributeValue;
    }
}
