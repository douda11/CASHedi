package com.example.cashedi.models;

import lombok.Data;
import java.util.List;

@Data
public class AcheelTarificationRequest {
    private String product;
    private String splitting;
    private String wish_effective_date;
    private String postal_code;
    private String city_id;
    private String subscriber_year_of_birth;
    private String subscriber_social_system;
    private String partner_year_of_birth;
    private String partner_social_system;
    private List<AdditionalMember> additional_member;

    @Data
    public static class AdditionalMember {
        private String year_of_birth;
        private String social_system;
        private String status;
    }
}
