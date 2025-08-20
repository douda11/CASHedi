package com.example.cashedi.models.apivia.devis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DevisList {
    private String ref;
    private List<DevisFile> files;
}
