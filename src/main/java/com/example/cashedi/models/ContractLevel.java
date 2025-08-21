package com.example.cashedi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractLevel {
    private String insurer;
    private String contractName;
    private String contractType;
    private String levelId;
    private String levelName;
    private Object benefits; // Can be Map<String, Object> for flexibility
    private Source source;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Source {
        private String pdfName;
        private String pageRange;
    }
}
