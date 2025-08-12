package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformationsResiliation {
    private boolean resiliation_infra_annuelle;
    private boolean remplacement_par_client;
}
