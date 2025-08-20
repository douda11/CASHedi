package com.example.cashedi.services;

import com.example.cashedi.models.TarificationRequestUtwin;
import com.example.cashedi.models.Proposition;
import com.example.cashedi.models.TarificationResponseUtwin;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TarificationServiceUtwin {

    private static final Logger logger = LoggerFactory.getLogger(TarificationServiceUtwin.class);

    private final WebClient webClient;

    @Value("${utwin.api.licence}")
    private String utwinLicence;

    @Value("${utwin.api.contexte-vendeur}")
    private String utwinContexteVendeur;

    public TarificationServiceUtwin(WebClient.Builder builder,
                                    @Value("${utwin.api.base-url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public Mono<TarificationResponseUtwin> getTarifs(TarificationRequestUtwin request) {
        return webClient.post()
                .uri("/api/Sante/v1/Tarifs")
                .header("Content-Type", "application/json")
                .header("Accept", MediaType.APPLICATION_JSON_VALUE) // Accepter JSON au lieu de text/plain
                .header("X-Utwin-Contexte-Vendeur", utwinContexteVendeur)
                .header("X-U-Licence", utwinLicence)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    logger.error("❌ Erreur 4xx Utwin API: Status={} Body={}", clientResponse.statusCode(), errorBody);
                                    return Mono.error(new RuntimeException("Erreur client Utwin: " + clientResponse.statusCode() + " - " + errorBody));
                                })
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    logger.error("❌ Erreur 5xx Utwin API: Status={} Body={}", clientResponse.statusCode(), errorBody);
                                    return Mono.error(new RuntimeException("Erreur serveur Utwin: " + clientResponse.statusCode() + " - " + errorBody));
                                })
                )
                .bodyToMono(TarificationResponseUtwin.class) // Désérialisation directe en TarificationResponseUtwin
                .map(response -> {
                    if (response != null && response.getPropositions() != null) {
                        List<Proposition> filteredPropositions = response.getPropositions().stream()
                                .filter(p -> "BASIC' Santé".equals(p.getLibelleProduit()) || "MULTI' Santé".equals(p.getLibelleProduit()))
                                .collect(Collectors.toList());
                        response.setPropositions(filteredPropositions);
                    }
                    return response;
                })
                .doOnSuccess(response -> {
                    if (response == null || (response.getMessages() != null && response.getMessages().isEmpty())) {
                        logger.warn("⚠️ La réponse de l'API est vide ou contient des messages vides.");
                    } else {
                        logger.info("✅ Réponse de l'API Utwin reçue avec succès : {}", response);
                    }
                })
                .doOnError(error -> {
                    logger.error("❌ Erreur lors de l’appel ou du traitement de la réponse Utwin : {}", error.getMessage(), error);
                });
    }

    public Mono<String> getDocument(String identifiant) {
        return webClient.get()
                .uri("/api/Sante/v1/Documents/{identifiant}", identifiant)
                .header("X-Utwin-Contexte-Vendeur", utwinContexteVendeur)
                .header("X-U-Licence", utwinLicence)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> logger.info("✅ Document Utwin {} récupéré avec succès.", identifiant))
                .doOnError(error -> logger.error("❌ Erreur lors de la récupération du document Utwin {}: {}", identifiant, error.getMessage(), error));
    }
}