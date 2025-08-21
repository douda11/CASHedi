package com.example.cashedi.services;

import com.example.cashedi.entites.Projet;
import com.example.cashedi.models.Archivage;
import com.example.cashedi.models.Signature;
import com.example.cashedi.entites.Projet;
import com.example.cashedi.repositories.ProjetRepository;
import com.example.cashedi.services.AlptisService;
import com.example.cashedi.models.AlptisTarificationResponse;
import com.example.cashedi.models.Produit;
import com.example.cashedi.services.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProjetServiceImpl implements ProjetService {

    private static final Logger logger = LoggerFactory.getLogger(ProjetServiceImpl.class);

    private final ProjetRepository projetRepository;
    private final AlptisService alptisService;
    private final ContractService contractService;

    @Autowired
    public ProjetServiceImpl(ProjetRepository projetRepository, AlptisService alptisService, ContractService contractService) {
        this.projetRepository = projetRepository;
        this.alptisService = alptisService;
        this.contractService = contractService;
    }

    @Override
    public Projet createProject(Projet projet) {
        logger.info("Attempting to create a new project with date_effet: {}", projet.getDateEffet());

        // Enrich user data before calling Alptis API
      

        AlptisTarificationResponse tarificationResponse = alptisService.getTarification(projet);

        if (tarificationResponse != null && tarificationResponse.getTarifs() != null) {
            projet.setTarifs(tarificationResponse.getTarifs());
            projet.setGenerationTarif(tarificationResponse.getGenerationTarif());
            projet.setCodeAssociation(tarificationResponse.getCodeAssociation());
            projet.setTypeCotisation(tarificationResponse.getTypeCotisation());
            projet.setMillesime(tarificationResponse.getMillesime());
            logger.info("Successfully retrieved and set tarification for the project.");
        } else {
            logger.error("Failed to retrieve tarification. Project creation aborted.");
            throw new RuntimeException("Failed to get tarification from Alptis API");
        }

        // Create the project in the Alptis system
        String alptisProjectId = alptisService.createAlptisProject(projet);
        if (alptisProjectId != null) {
            projet.setAlptisProjectId(alptisProjectId);
            logger.info("Project successfully created in Alptis with ID: {}", alptisProjectId);
        } else {
            logger.error("Failed to create project in Alptis system. Local project creation aborted.");
            throw new RuntimeException("Failed to create project in Alptis system");
        }

        projet.setDateCreation(new Date());
        projet.setDateModification(new Date());
        projet.setStatut("Brouillon");

        // Générer un clientId temporaire. La logique métier réelle pour le clientId doit être implémentée ici.
        projet.setClientId((long) (Math.random() * 10000));

        // Initialiser les objets imbriqués s'ils sont nuls
        if (projet.getSignature() == null) {
            projet.setSignature(new Signature());
        }
        if (projet.getArchivage() == null) {
            projet.setArchivage(new Archivage());
        }

        Projet savedProjet = projetRepository.save(projet);
        logger.info("Project successfully saved in local DB with ID: {}", savedProjet.getId());

        return savedProjet;
    }

    @Override
    public Optional<Projet> getProjectById(String projetId) {
        Optional<Projet> projetOpt = projetRepository.findById(projetId);

        projetOpt.ifPresent(projet -> {
            try {
                AlptisTarificationResponse tarificationResponse = alptisService.getTarification(projet);
                if (tarificationResponse != null) {
                    projet.setTarifs(tarificationResponse.getTarifs());
                    projet.setTypeCotisation(tarificationResponse.getTypeCotisation());
                    projet.setGenerationTarif(tarificationResponse.getGenerationTarif());
                    projet.setCodeAssociation(tarificationResponse.getCodeAssociation());
                    projet.setMillesime(tarificationResponse.getMillesime());
                    
                    // Don't pre-populate produits - let user choose from comparison results
                    // The produits field will be populated when user selects a specific product
                    
                    logger.info("Successfully enriched projet with Alptis tarification data: generationTarif={}, codeAssociation={}", 
                        tarificationResponse.getGenerationTarif(), tarificationResponse.getCodeAssociation());
                } else {
                    logger.warn("Failed to get tarification data from Alptis API for projet {}", projetId);
                }
            } catch (Exception e) {
                logger.error("Error fetching tarification for project {}", projet.getId(), e);
                // The project will be returned without tarification data
            }
        });

        return projetOpt;
    }

    @Override
    public Projet updateProject(String projetId, Projet projetDetails) {
        return projetRepository.findById(projetId).map(projet -> {
            projet.setDateModification(new Date());

            if (projetDetails.getDateEffet() != null) {
                projet.setDateEffet(projetDetails.getDateEffet());
            }
            if (projetDetails.getCommissionnement() != null) {
                projet.setCommissionnement(projetDetails.getCommissionnement());
            }
            if (projetDetails.getOffre() != null) {
                projet.setOffre(projetDetails.getOffre());
            }
            if (projetDetails.getAssures() != null) {
                projet.setAssures(projetDetails.getAssures());
            }
            if (projetDetails.getPaiement() != null) {
                projet.setPaiement(projetDetails.getPaiement());
            }
            if (projetDetails.getRemboursement() != null) {
                projet.setRemboursement(projetDetails.getRemboursement());
            }
            if (projetDetails.getResiliationInfraAnnuelle() != null) {
                projet.setResiliationInfraAnnuelle(projetDetails.getResiliationInfraAnnuelle());
            }
            if (projetDetails.getUtilisateur() != null) {
                projet.setUtilisateur(projetDetails.getUtilisateur());
            }
            if (projetDetails.getSignature() != null) {
                projet.setSignature(projetDetails.getSignature());
            }

            return projetRepository.save(projet);
        }).orElse(null); // Consider throwing a custom exception e.g., ResourceNotFoundException
    }

    @Override
    public Projet selectProductForProject(String projetId, Produit selectedProduct) {
        Optional<Projet> projetOpt = projetRepository.findById(projetId);
        
        if (projetOpt.isPresent()) {
            Projet projet = projetOpt.get();
            
            // Set the selected product
            List<Produit> produits = new ArrayList<>();
            produits.add(selectedProduct);
            projet.setProduits(produits);
            
            // Update modification date
            projet.setDateModification(new Date());
            
            logger.info("Product selected for project {}: productId={}, productCode={}", 
                projetId, selectedProduct.getId(), selectedProduct.getCode());
            
            return projetRepository.save(projet);
        } else {
            throw new RuntimeException("Project not found with id: " + projetId);
        }
    }
}
