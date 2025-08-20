package com.example.cashedi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Base64;
import java.util.Optional;

@Service
public class DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);
    private final AprilApiService aprilApiService;

    @Autowired
    public DocumentService(AprilApiService aprilApiService) {
        this.aprilApiService = aprilApiService;
    }

    /**
     * Récupère le contenu d'un document par son identifiant, l'encode en Base64 et le retourne.
     *
     * @param identifiant L'identifiant technique du document.
     * @return Un Optional contenant le contenu du document en Base64, ou vide si non trouvé.
     */
    public Optional<String> getDocumentContentById(String identifiant) {
        try {
            byte[] documentBytes = aprilApiService.getDocument(identifiant);

            if (documentBytes != null && documentBytes.length > 0) {
                String base64Content = Base64.getEncoder().encodeToString(documentBytes);
                return Optional.of(base64Content);
            } else {
                logger.warn("Le document avec l'identifiant {} a été trouvé mais son contenu est vide.", identifiant);
                return Optional.empty();
            }

        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Aucun document trouvé pour l'identifiant: {}", identifiant);
            return Optional.empty();

        } catch (Exception e) {
            logger.error("Une erreur est survenue lors de la récupération du document avec l'identifiant: {}", identifiant, e);
            throw new RuntimeException("Erreur interne lors de la récupération du document.", e);
        }
    }
}
