package com.example.cashedi.controllers;

import com.example.cashedi.models.alptis.devis.AlptisGeneratePdfDevisRequest;
import com.example.cashedi.models.alptis.devis.AlptisGeneratePdfDevisResponse;
import com.example.cashedi.services.AlptisTarificationService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Base64;

@RestController
@RequestMapping("/api/alptis")
public class AlptisQuoteController {

    private static final Logger logger = LoggerFactory.getLogger(AlptisQuoteController.class);

    private final AlptisTarificationService tarificationService;

    public AlptisQuoteController(AlptisTarificationService tarificationService) {
        this.tarificationService = tarificationService;
    }

    @PostMapping("/generate-devis")
    public Mono<ResponseEntity<byte[]>> generateDevis(
            @Valid @RequestBody AlptisGeneratePdfDevisRequest request,
            @RequestParam(value = "test", defaultValue = "false") boolean isTest) {
        
        logger.info("Received request for ALPTIS devis generation (test={}): {}", isTest, request);

        Mono<AlptisGeneratePdfDevisResponse> responseMono = isTest ? 
            tarificationService.testGeneratePdfDevis(request) : 
            tarificationService.generatePdfDevis(request);

        return responseMono
                .<ResponseEntity<byte[]>>map(response -> {
                    try {
                        if (response.getFichierPdf() != null) {
                            // Decode Base64 PDF content
                            byte[] pdfBytes = Base64.getDecoder().decode(response.getFichierPdf());
                            
                            // Set appropriate headers for PDF download
                            HttpHeaders headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_PDF);
                            headers.setContentDispositionFormData("attachment", 
                                response.getNomFichier() != null ? response.getNomFichier() : "devis_alptis.pdf");
                            headers.setContentLength(pdfBytes.length);
                            
                            logger.info("Successfully generated ALPTIS PDF devis: {} bytes", pdfBytes.length);
                            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
                        } else {
                            logger.error("No PDF content received from ALPTIS API");
                            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                        }
                    } catch (Exception e) {
                        logger.error("Error processing ALPTIS PDF response", e);
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                })
                .onErrorResume(e -> {
                    logger.error("Error generating ALPTIS devis: {}", e.getMessage(), e);
                    return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }
}
