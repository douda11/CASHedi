package com.example.cashedi.models.apivia;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class ApiviaTarificationResponse {
    private String action;
    private String status;
    private Object list;
}
