package com.example.cashedi.models;

import lombok.Data;

@Data
public class Document {
    private String identifiant;
    private String type;
    private String nom;
    private String format;
}