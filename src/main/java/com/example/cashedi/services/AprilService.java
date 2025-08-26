package com.example.cashedi.services;

import com.example.cashedi.models.AprilProfessionalCategory;
import com.example.cashedi.models.AprilProfession;
import com.example.cashedi.models.AprilProfessionalStatus;

import java.util.List;

public interface AprilService {
    List<AprilProfessionalCategory> getProfessionalCategories();
    List<AprilProfession> getProfessions();
    List<AprilProfessionalStatus> getProfessionalStatuses(String productCode);
}
