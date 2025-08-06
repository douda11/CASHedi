package com.example.cashedi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InsuredRef {
    @JsonProperty("$ref")
    private String ref;

    // Getter & Setter
    public String getRef()   { return ref; }
    public void setRef(String ref) { this.ref = ref; }


}
