package com.example.cashedi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {
    @JsonProperty("$id")
    private String id;
    private String birthDate;
    private String title;
    private String lastName;
    private String birthName;
    private String firstName;
    private String birthDepartment;
    private String birthCity;
    private String nationality;
    private boolean politicallyExposedPerson;
    private String birthCountry;
    private String mandatoryScheme;
    private String professionalCategory;
    private String familyStatus;
    private String profession;
    private boolean acceptanceRequestPartnersAPRIL;
    private boolean acreBeneficiary;
    private String companyCreationDate;
    private boolean swissCrossBorderWorker;
    private boolean businessCreator;
    private boolean microEntrepreneur;
    private String socialSecurityNumber;
    private LandlinePhone landlinePhone;
    private MobilePhone mobilePhone;
    private String email;

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getBirthName() { return birthName; }
    public void setBirthName(String birthName) { this.birthName = birthName; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getBirthDepartment() { return birthDepartment; }
    public void setBirthDepartment(String birthDepartment) { this.birthDepartment = birthDepartment; }
    public String getBirthCity() { return birthCity; }
    public void setBirthCity(String birthCity) { this.birthCity = birthCity; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public boolean isPoliticallyExposedPerson() { return politicallyExposedPerson; }
    public void setPoliticallyExposedPerson(boolean politicallyExposedPerson) { this.politicallyExposedPerson = politicallyExposedPerson; }
    public String getBirthCountry() { return birthCountry; }
    public void setBirthCountry(String birthCountry) { this.birthCountry = birthCountry; }
    public String getMandatoryScheme() { return mandatoryScheme; }
    public void setMandatoryScheme(String mandatoryScheme) { this.mandatoryScheme = mandatoryScheme; }
    public String getProfessionalCategory() { return professionalCategory; }
    public void setProfessionalCategory(String professionalCategory) { this.professionalCategory = professionalCategory; }
    public String getFamilyStatus() { return familyStatus; }
    public void setFamilyStatus(String familyStatus) { this.familyStatus = familyStatus; }
    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }
    public boolean isAcceptanceRequestPartnersAPRIL() { return acceptanceRequestPartnersAPRIL; }
    public void setAcceptanceRequestPartnersAPRIL(boolean acceptanceRequestPartnersAPRIL) { this.acceptanceRequestPartnersAPRIL = acceptanceRequestPartnersAPRIL; }
    public boolean isAcreBeneficiary() { return acreBeneficiary; }
    public void setAcreBeneficiary(boolean acreBeneficiary) { this.acreBeneficiary = acreBeneficiary; }
    public String getCompanyCreationDate() { return companyCreationDate; }
    public void setCompanyCreationDate(String companyCreationDate) { this.companyCreationDate = companyCreationDate; }
    public boolean isSwissCrossBorderWorker() { return swissCrossBorderWorker; }
    public void setSwissCrossBorderWorker(boolean swissCrossBorderWorker) { this.swissCrossBorderWorker = swissCrossBorderWorker; }
    public boolean isBusinessCreator() { return businessCreator; }
    public void setBusinessCreator(boolean businessCreator) { this.businessCreator = businessCreator; }
    public boolean isMicroEntrepreneur() { return microEntrepreneur; }
    public void setMicroEntrepreneur(boolean microEntrepreneur) { this.microEntrepreneur = microEntrepreneur; }
    public String getSocialSecurityNumber() { return socialSecurityNumber; }
    public void setSocialSecurityNumber(String socialSecurityNumber) { this.socialSecurityNumber = socialSecurityNumber; }
    public LandlinePhone getLandlinePhone() { return landlinePhone; }
    public void setLandlinePhone(LandlinePhone landlinePhone) { this.landlinePhone = landlinePhone; }
    public MobilePhone getMobilePhone() { return mobilePhone; }
    public void setMobilePhone(MobilePhone mobilePhone) { this.mobilePhone = mobilePhone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
