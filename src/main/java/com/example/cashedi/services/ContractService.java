package com.example.cashedi.services;

import com.example.cashedi.models.Produit;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ContractService {
    
    // Mapping des niveaux Alptis vers les IDs de produits
    private static final Map<String, Integer> ALPTIS_LEVEL_TO_PRODUCT_ID = new HashMap<>();
    
    static {
        ALPTIS_LEVEL_TO_PRODUCT_ID.put("alptis_sante_pro_plus_lvl1", 495);
        ALPTIS_LEVEL_TO_PRODUCT_ID.put("alptis_sante_pro_plus_lvl2", 496);
        ALPTIS_LEVEL_TO_PRODUCT_ID.put("alptis_sante_pro_plus_lvl3", 497);
        ALPTIS_LEVEL_TO_PRODUCT_ID.put("alptis_sante_pro_plus_lvl4", 498);
        ALPTIS_LEVEL_TO_PRODUCT_ID.put("alptis_sante_pro_plus_lvl5", 499);
        ALPTIS_LEVEL_TO_PRODUCT_ID.put("alptis_sante_pro_plus_lvl6", 500);
    }
    
    public Produit getProductByOfferLevel(String offerLevel) {
        // Construire le level_id basé sur le niveau de l'offre
        String levelId = "alptis_sante_pro_plus_lvl" + offerLevel;
        
        Integer productId = ALPTIS_LEVEL_TO_PRODUCT_ID.get(levelId);
        if (productId != null) {
            Produit produit = new Produit();
            produit.setId(productId);
            produit.setCode("SANTE PRO +");
            return produit;
        }
        
        // Fallback vers niveau 1 par défaut
        Produit defaultProduit = new Produit();
        defaultProduit.setId(495);
        defaultProduit.setCode("SANTE PRO +");
        return defaultProduit;
    }
    
    public String getContractNameByLevel(String level) {
        return "Santé Pro+";
    }
    
    public String getInsurerName() {
        return "Alptis";
    }
}
