package com.example.cashedi.services;

import com.example.cashedi.models.DocumentInfo;
import java.util.List;
import java.util.Optional;

public interface DocumentAlptisService {
    Optional<List<DocumentInfo>> getDocumentsForProject(String projetId);
    Optional<byte[]> generateEtudePersonnalisee(String projetId);
    Optional<byte[]> getStaticDocument(String code);
}
