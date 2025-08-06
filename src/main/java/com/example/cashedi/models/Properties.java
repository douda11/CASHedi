package com.example.cashedi.models;

import java.util.List;

public class Properties {
    private List<Address> addresses;
    private String email;

    // Getters & Setters
    public List<Address> getAddresses() { return addresses; }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
