package com.example.cashedi.services;

import com.example.cashedi.models.apivia.ApiviaTarificationRequest;
import com.example.cashedi.models.apivia.ApiviaTarificationResponse;
import com.example.cashedi.models.apivia.ApiviaBeneficiaire;
import com.example.cashedi.models.apivia.devis.Adresse;
import com.example.cashedi.models.apivia.devis.GeneratePdfDevisRequest;
import com.example.cashedi.models.apivia.devis.GeneratePdfDevisResponse;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

/**
 * Service for interacting with the Apivia Tarification API.
 */
@Service
public class ApiviaTarificationService {

    private static final Logger logger = LoggerFactory.getLogger(ApiviaTarificationService.class);

    private final WebClient webClient;

    private final String apiKey = "f85d0eb55e8e069acb908c1d11af4c6e";
    private final String apiUrl = "https://extranet-recette.apivia-courtage.fr/ws/rest/sante/individuelle/";

    public ApiviaTarificationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<ApiviaTarificationResponse> getProduits() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("cle", apiKey);
        formData.add("format", "json");
        formData.add("action", "liste_produit");

        logger.info("Calling APIVIA for product list...");

        return webClient.post()
                .uri(this.apiUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(ApiviaTarificationResponse.class)
                .doOnSuccess(response -> logger.info("Successfully retrieved product list."))
                .doOnError(error -> logger.error("Error fetching product list from APIVIA: {}", error.getMessage()));
    }

    public Mono<ApiviaTarificationResponse> getTarification(ApiviaTarificationRequest request) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("cle", apiKey);
            formData.add("format", request.getFormat());
            formData.add("action", request.getAction());
            formData.add("produits", request.getProduits());
            formData.add("formules", request.getFormules());
            formData.add("renforts", request.getRenforts());
            formData.add("options", request.getOptions());
            formData.add("codePostal", request.getCodePostal());
            formData.add("dateEffet", request.getDateEffet());
            formData.add("dejaAssurance", String.valueOf(request.isDejaAssurance()));
            formData.add("isResiliationPourCompte", String.valueOf(request.isResiliationPourCompte()));
            formData.add("typeAffaire", request.getTypeAffaire());
            formData.add("typeObjectSortie", request.getTypeObjectSortie());

            // The API expects beneficiaries as an array of form fields, not a JSON string.
            // Example: beneficiaires[0][typeBeneficiaire] = 'PRINCIPAL'
            if (request.getBeneficiaires() != null) {
                for (int i = 0; i < request.getBeneficiaires().size(); i++) {
                    ApiviaBeneficiaire beneficiaire = request.getBeneficiaires().get(i);
                    String base = "beneficiaires[" + i + "]";
                    formData.add(base + "[typeBeneficiaire]", beneficiaire.getTypeBeneficiaire());
                    formData.add(base + "[dateDeNaissance]", beneficiaire.getDateDeNaissance());
                    formData.add(base + "[typeRegime]", beneficiaire.getTypeRegime());
                    formData.add(base + "[regimeSocial]", beneficiaire.getRegimeSocial());
                    if (beneficiaire.getTypeAutreBeneficiaire() != null) {
                        formData.add(base + "[typeAutreBeneficiaire]", beneficiaire.getTypeAutreBeneficiaire());
                    }
                }
            }

            logger.info("Calling APIVIA Tarification API at: {} with form data", this.apiUrl);

            return webClient.post()
                    .uri(this.apiUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(ApiviaTarificationResponse.class)
                    .doOnError(error -> logger.error("Error calling APIVIA tarification API", error));

        } catch (Exception e) {
            logger.error("Error preparing APIVIA request", e);
            return Mono.error(e);
        }
    }

    public Mono<GeneratePdfDevisResponse> testGeneratePdfDevis(GeneratePdfDevisRequest request) {
        request.setAction("test_generate_pdf_devis");
        return getTarifAndGeneratePdf(request);
    }

    public Mono<GeneratePdfDevisResponse> generatePdfDevis(GeneratePdfDevisRequest request) {
        request.setAction("generate_pdf_devis");
        return getTarifAndGeneratePdf(request);
    }

    private Mono<GeneratePdfDevisResponse> getTarifAndGeneratePdf(GeneratePdfDevisRequest request) {
        // Validate request data before calling API
        String validationError = validateRequestData(request);
        if (validationError != null) {
            return Mono.error(new RuntimeException("Validation error: " + validationError));
        }
        
        // Step 1: Call Tarification API to get the correct rate
        ApiviaTarificationRequest tarifRequest = new ApiviaTarificationRequest();
        tarifRequest.setFormat("json");
        tarifRequest.setAction("tarification");
        tarifRequest.setProduits(request.getProduit());
        tarifRequest.setFormules(request.getFormule());
        tarifRequest.setDateEffet(request.getDateEffet());
        tarifRequest.setBeneficiaires(request.getBeneficiaires());
        // Assuming the main beneficiary's postal code is the one to use for tarification
        if (request.getSouscripteur() != null && request.getSouscripteur().getAdresse() != null) {
            tarifRequest.setCodePostal(request.getSouscripteur().getAdresse().getCodePostal());
        }

        return getTarification(tarifRequest).flatMap(tarifResponse -> {
            // Step 2: Extract the tarif from the response and call PDF generation
            Object listObject = tarifResponse.getList();
            try {
                if (listObject instanceof List && !((List<?>) listObject).isEmpty()) {
                    @SuppressWarnings("unchecked")
                    List<java.util.Map<String, Object>> productList = (List<java.util.Map<String, Object>>) listObject;
                    java.util.Map<String, Object> productMap = productList.get(0);

                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> formulesMap = (java.util.Map<String, Object>) productMap.get("formules");
                    
                    String requestedFormule = request.getFormule();
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> formuleDetails = (java.util.Map<String, Object>) formulesMap.get(requestedFormule);

                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> baseTarifMap = (java.util.Map<String, Object>) formuleDetails.get("base");

                    Double correctTarif = (Double) baseTarifMap.get("complete");

                    request.setTarif(correctTarif.floatValue());
                    return callGeneratePdfApi(request);
                }
            } catch (Exception e) {
                logger.error("Failed to parse tarif from Apivia response", e);
            }
            return Mono.error(new RuntimeException("Could not retrieve a valid tarif from Apivia API. Response list: " + listObject));
        });
    }

    private String validateRequestData(GeneratePdfDevisRequest request) {
        // Date d'effet validation (simple regex for DD/MM/YYYY)
        if (request.getDateEffet() == null || !request.getDateEffet().matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$")) {
            return "Invalid date format for dateEffet. Expected DD/MM/YYYY.";
        }

        if (request.getSouscripteur() == null) {
            return "Souscripteur is missing.";
        }

        com.example.cashedi.models.apivia.devis.Adresse adresse = request.getSouscripteur().getAdresse();
        if (adresse == null) {
            return "Adresse for souscripteur is missing.";
        }

        // Code Postal validation
        if (adresse.getCodePostal() == null || !adresse.getCodePostal().matches("^\\d{5}$")) {
            return "Invalid codePostal. Expected 5 digits.";
        }

        // Required address fields validation based on documentation
        if (adresse.getNomVoie() == null || adresse.getNomVoie().isEmpty()) {
            return "nomVoie is required in the address.";
        }
        if (adresse.getNatureVoie() == null || adresse.getNatureVoie().isEmpty()) {
            return "natureVoie is required in the address.";
        }
        if (adresse.getVille() == null || adresse.getVille().isEmpty()) {
            return "ville is required in the address.";
        }

        return null; // No validation errors
    }

    private Mono<GeneratePdfDevisResponse> callGeneratePdfApi(GeneratePdfDevisRequest request) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("cle", apiKey);
            formData.add("format", request.getFormat());
            formData.add("action", request.getAction());
            formData.add("produit", request.getProduit());
            formData.add("formule", request.getFormule());
            formData.add("dateEffet", request.getDateEffet());
            formData.add("tarif", String.valueOf(request.getTarif()));
            formData.add("frais", request.getFrais());
            formData.add("dejaAssurance", String.valueOf(request.isDejaAssurance()));
            formData.add("isResiliationPourCompte", String.valueOf(request.isResiliationPourCompte()));

            // Flatten Conseiller
            if (request.getConseiller() != null) {
                formData.add("conseiller[nom]", request.getConseiller().getNom());
                formData.add("conseiller[prenom]", request.getConseiller().getPrenom());
                formData.add("conseiller[typeConseiller]", request.getConseiller().getTypeConseiller());
                formData.add("conseiller[orias]", request.getConseiller().getOrias());
            }

            // Flatten Souscripteur
            if (request.getSouscripteur() != null) {
                formData.add("souscripteur[typeCivilite]", request.getSouscripteur().getTypeCivilite());
                formData.add("souscripteur[nom]", request.getSouscripteur().getNom());
                formData.add("souscripteur[prenom]", request.getSouscripteur().getPrenom());
                formData.add("souscripteur[telephone]", request.getSouscripteur().getTelephone());
                formData.add("souscripteur[mobile]", request.getSouscripteur().getMobile());
                formData.add("souscripteur[email]", request.getSouscripteur().getEmail());
                if (request.getSouscripteur().getAdresse() != null) {
                    Adresse adresse = request.getSouscripteur().getAdresse();
                    formData.add("souscripteur[adresse][numero]", adresse.getNumero());
                    formData.add("souscripteur[adresse][codeBtq]", adresse.getCodeBtq());
                    formData.add("souscripteur[adresse][natureVoie]", adresse.getNatureVoie());
                    formData.add("souscripteur[adresse][nomVoie]", adresse.getNomVoie());
                    formData.add("souscripteur[adresse][complement]", adresse.getComplement());
                    formData.add("souscripteur[adresse][codePostal]", adresse.getCodePostal());
                    formData.add("souscripteur[adresse][ville]", adresse.getVille());

                    // Build and add the 'adresseComplete' string
                    StringBuilder sb = new StringBuilder();
                    if (adresse.getNumero() != null && !adresse.getNumero().isEmpty()) sb.append(adresse.getNumero()).append(" ");
                    if (adresse.getCodeBtq() != null && !adresse.getCodeBtq().isEmpty()) sb.append(adresse.getCodeBtq()).append(" ");
                    if (adresse.getNatureVoie() != null && !adresse.getNatureVoie().isEmpty()) sb.append(adresse.getNatureVoie()).append(" ");
                    if (adresse.getNomVoie() != null && !adresse.getNomVoie().isEmpty()) sb.append(adresse.getNomVoie());
                    
                    String fullAddress = sb.toString().trim();
                    formData.add("souscripteur[adresse][adresseComplete]", fullAddress);
                    formData.add("souscripteur[adresse][isAdresseComplete]", String.valueOf(adresse.isAdresseEstComplete()));
                }
            }

            // Flatten Beneficiaires
            if (request.getBeneficiaires() != null) {
                for (int i = 0; i < request.getBeneficiaires().size(); i++) {
                    ApiviaBeneficiaire beneficiaire = request.getBeneficiaires().get(i);
                    String base = "beneficiaires[" + i + "]";
                    formData.add(base + "[typeBeneficiaire]", beneficiaire.getTypeBeneficiaire());
                    formData.add(base + "[dateDeNaissance]", beneficiaire.getDateDeNaissance());
                    formData.add(base + "[typeRegime]", beneficiaire.getTypeRegime());
                    formData.add(base + "[regimeSocial]", beneficiaire.getRegimeSocial());
                    // Use beneficiary's postal code if available, otherwise default to subscriber's postal code.
                    String codePostal = null;
                    if (beneficiaire.getAdresse() != null && beneficiaire.getAdresse().getCodePostal() != null) {
                        codePostal = beneficiaire.getAdresse().getCodePostal();
                    } else if (request.getSouscripteur() != null && request.getSouscripteur().getAdresse() != null) {
                        codePostal = request.getSouscripteur().getAdresse().getCodePostal();
                    }

                    if (codePostal != null) {
                        formData.add(base + "[adresse][codePostal]", codePostal);
                    }
                }
            }

            logger.info("Calling APIVIA PDF Generation API with action: {}", request.getAction());
            logger.info("Payload being sent: {}", formData.toString());

            return webClient.post()
                    .uri(this.apiUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(GeneratePdfDevisResponse.class)
                    .doOnSuccess(response -> logger.info("Successfully called PDF generation API."))
                    .doOnError(error -> logger.error("Error calling APIVIA PDF generation API: {}", error.getMessage()));

        } catch (Exception e) {
            logger.error("Error preparing APIVIA PDF generation request", e);
            return Mono.error(e);
        }
    }
}