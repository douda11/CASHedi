package com.example.cashedi.models.apivia.devis;

import lombok.Data;

import java.util.List;

@Data
public class DevisResponseList {
    private String ref;
    private List<DevisFile> files;
    private String message; // For test_generate_pdf_devis
}
