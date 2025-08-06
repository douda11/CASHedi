package com.example.cashedi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Payload {

    @JsonProperty("$type")
    private String type;
    private Properties properties;
    private List<Person> persons;
    private List<Product> products;

    // Getters & Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Properties getProperties() { return properties; }
    public void setProperties(Properties properties) { this.properties = properties; }
    public List<Person> getPersons() { return persons; }
    public void setPersons(List<Person> persons) { this.persons = persons; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}