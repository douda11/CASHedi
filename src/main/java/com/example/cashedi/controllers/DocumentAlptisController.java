package com.example.cashedi.controllers;

import com.example.cashedi.models.DocumentInfo;
import com.example.cashedi.services.DocumentAlptisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alptis/v1")
public class DocumentAlptisController {

    private final DocumentAlptisService documentAlptisService;

    @Autowired
    public DocumentAlptisController(DocumentAlptisService documentAlptisService) {
        this.documentAlptisService = documentAlptisService;
    }

    @GetMapping("/projets/{projetId}/documents")
    public ResponseEntity<List<DocumentInfo>> getDocumentsForProject(@PathVariable String projetId) {
        return documentAlptisService.getDocumentsForProject(projetId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/projets/{projetId}/etude-personnalisee")
    public ResponseEntity<byte[]> getEtudePersonnalisee(@PathVariable String projetId) {
        Optional<byte[]> pdfContentOpt = documentAlptisService.generateEtudePersonnalisee(projetId);
        if (pdfContentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setContentDispositionFormData("attachment", "Etude-Personnalisee-Alptis-" + projetId + ".pdf");
        return new ResponseEntity<>(pdfContentOpt.get(), headers, HttpStatus.OK);
    }

    @GetMapping("/documents-statiques")
    public ResponseEntity<byte[]> getStaticDocument(@RequestParam String code) {
        Optional<byte[]> pdfContentOpt = documentAlptisService.getStaticDocument(code);
        if (pdfContentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.setContentDispositionFormData("attachment", code + "-Alptis.pdf");
        return new ResponseEntity<>(pdfContentOpt.get(), headers, HttpStatus.OK);
    }
}
