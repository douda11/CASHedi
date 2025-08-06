package com.example.cashedi.services;

import com.example.cashedi.models.AcheelTarificationRequest;
import com.example.cashedi.models.AcheelTarificationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DeuxMaTarificationService {

    private final WebClient webClient;
    private final DeuxMaAuthService authService;

    @Value("${deuxma.api.url:https://dev.ws.sante.2ma.fr}")
    private String apiUrl;

    public DeuxMaTarificationService(WebClient.Builder webClientBuilder, DeuxMaAuthService authService) {
        this.webClient = webClientBuilder.build();
        this.authService = authService;
    }

    public Mono<AcheelTarificationResponse> getAcheelTarif(AcheelTarificationRequest request) {
        return authService.authenticate().flatMap(token -> {
            MultiValueMap<String, String> formData = buildFormData(request);

            return webClient.post()
                    .uri(apiUrl + "/AcheelController.php?service=GENERATE_TARIF_ACHEEL")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(AcheelTarificationResponse.class);
        });
    }

    private MultiValueMap<String, String> buildFormData(AcheelTarificationRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("product", request.getProduct());
        formData.add("splitting", request.getSplitting());
        formData.add("wish_effective_date", request.getWish_effective_date());
        formData.add("postal_code", request.getPostal_code());
        formData.add("city_id", request.getCity_id());
        formData.add("subscriber_year_of_birth", request.getSubscriber_year_of_birth());
        formData.add("subscriber_social_system", request.getSubscriber_social_system());

        if (request.getPartner_year_of_birth() != null) {
            formData.add("partner_year_of_birth", request.getPartner_year_of_birth());
            formData.add("partner_social_system", request.getPartner_social_system());
        }

        if (request.getAdditional_member() != null) {
            for (int i = 0; i < request.getAdditional_member().size(); i++) {
                AcheelTarificationRequest.AdditionalMember member = request.getAdditional_member().get(i);
                formData.add(String.format("additional_member[%d][year_of_birth]", i), member.getYear_of_birth());
                formData.add(String.format("additional_member[%d][social_system]", i), member.getSocial_system());
                formData.add(String.format("additional_member[%d][status]", i), member.getStatus());
            }
        }

        return formData;
    }
}
