package com.example.cashedi.controllers;

import com.example.cashedi.models.AprilProfessionalCategory;
import com.example.cashedi.models.AprilProfession;
import com.example.cashedi.models.AprilProfessionalStatus;
import com.example.cashedi.services.AprilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class AprilController {

    @Autowired
    private AprilService aprilService;

    @GetMapping("/healthProtection/professionalCategories")
    public ResponseEntity<List<AprilProfessionalCategory>> getProfessionalCategories() {
        try {
            System.out.println("Controller: Received request for professional categories");
            List<AprilProfessionalCategory> categories = aprilService.getProfessionalCategories();
            System.out.println("Controller: Successfully retrieved " + categories.size() + " categories");
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            System.err.println("Controller error in getProfessionalCategories: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/healthProtection/professions")
    public ResponseEntity<List<AprilProfession>> getProfessions() {
        try {
            System.out.println("Controller: Received request for professions");
            List<AprilProfession> professions = aprilService.getProfessions();
            System.out.println("Controller: Successfully retrieved " + professions.size() + " professions");
            return ResponseEntity.ok(professions);
        } catch (Exception e) {
            System.err.println("Controller error in getProfessions: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/healthProtection/products/{productCode}/professionalStatuses")
    public ResponseEntity<List<AprilProfessionalStatus>> getProfessionalStatuses(@PathVariable String productCode) {
        try {
            System.out.println("Controller: Received request for professional statuses with productCode: " + productCode);
            List<AprilProfessionalStatus> statuses = aprilService.getProfessionalStatuses(productCode);
            System.out.println("Controller: Successfully retrieved " + statuses.size() + " statuses");
            return ResponseEntity.ok(statuses);
        } catch (Exception e) {
            System.err.println("Controller error in getProfessionalStatuses: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
