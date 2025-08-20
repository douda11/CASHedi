package com.example.cashedi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Product {
    @JsonProperty("$id")
    private String id;
    private String productCode;
    private String effectiveDate;
    private String commission;
    private Termination termination;
    private List<Insured> insureds;
    private List<Coverage> coverages;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(String effectiveDate) { this.effectiveDate = effectiveDate; }
    public String getCommission() { return commission; }
    public void setCommission(String commission) { this.commission = commission; }
    public Termination getTermination() { return termination; }
    public void setTermination(Termination termination) { this.termination = termination; }
    public List<Insured> getInsureds() { return insureds; }
    public void setInsureds(List<Insured> insureds) { this.insureds = insureds; }
    public List<Coverage> getCoverages() { return coverages; }
    public void setCoverages(List<Coverage> coverages) { this.coverages = coverages; }
}
