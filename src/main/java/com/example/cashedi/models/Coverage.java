package com.example.cashedi.models;

public class Coverage {
    private InsuredRef insured;  // class InsuredRef defined below
    private String guaranteeCode;
    private String levelCode;
    private boolean eligibleMadelinLaw;

    // Getters & Setters
    public InsuredRef getInsured() { return insured; }
    public void setInsured(InsuredRef insured) { this.insured = insured; }
    public String getGuaranteeCode() { return guaranteeCode; }
    public void setGuaranteeCode(String guaranteeCode) { this.guaranteeCode = guaranteeCode; }
    public String getLevelCode() { return levelCode; }
    public void setLevelCode(String levelCode) { this.levelCode = levelCode; }
    public boolean isEligibleMadelinLaw() { return eligibleMadelinLaw; }
    public void setEligibleMadelinLaw(boolean eligibleMadelinLaw) { this.eligibleMadelinLaw = eligibleMadelinLaw; }
}
