package com.example.cashedi.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Insured {
    @JsonProperty("$id")
    private String id;
    private String role;
    private Ref person;  // class Ref defined below

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Ref getPerson() { return person; }
    public void setPerson(Ref person) { this.person = person; }
}
