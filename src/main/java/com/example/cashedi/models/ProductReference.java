package com.example.cashedi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true) // Ignore JSON fields not defined in this POJO
public class ProductReference {

    @JsonProperty("productCode")
    private String productCode;

    @JsonProperty("productTitle")
    private String productTitle;

    public ProductReference() {
        // Default constructor needed for Jackson deserialization
    }

    // Getters
    public String getProductCode() {
        return productCode;
    }

    public String getProductTitle() {
        return productTitle;
    }

    // Setters
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    @Override
    public String toString() {
        return "ProductReference{" +
                "productCode='" + productCode + '\'' +
                ", productTitle='" + productTitle + '\'' +
                '}';
    }
}
