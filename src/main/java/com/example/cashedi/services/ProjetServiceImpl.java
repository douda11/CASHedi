package com.example.cashedi.services;

import com.example.cashedi.entites.Projet;
import com.example.cashedi.models.Archivage;
import com.example.cashedi.models.Signature;
import com.example.cashedi.models.Tarifs;
import com.example.cashedi.repositories.ProjetRepository;
import com.example.cashedi.services.AlptisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ProjetServiceImpl implements ProjetService {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProjetServiceImpl.class);

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private AlptisService alptisService;

    @Override
    public Projet createProject(Projet projet) {
        // Initialiser les champs gérés par le serveur
        projet.setDateCreation(new Date());
        projet.setDateModification(new Date());
        projet.setStatut("NOUVEAU");
        projet.setEtat("EN_COURS");

        // Générer un clientId temporaire. La logique métier réelle pour le clientId doit être implémentée ici.
        projet.setClientId((long) (Math.random() * 10000));

        // Initialiser les objets imbriqués s'ils sont nuls
        if (projet.getSignature() == null) {
            projet.setSignature(new Signature());
        }
        if (projet.getArchivage() == null) {
            projet.setArchivage(new Archivage());
        }

        // À ce stade, vous pourriez appeler un service de tarification pour remplir projet.setTarifs(...)

        return projetRepository.save(projet);
    }

    @Override
    public Optional<Projet> getProjectById(String projetId) {
        Optional<Projet> projetOpt = projetRepository.findById(projetId);

        projetOpt.ifPresent(projet -> {
            try {
                Tarifs tarifs = alptisService.getTarification(projet);
                if (tarifs != null) {
                    projet.setTarifs(tarifs);
                    projet.setTypeCotisation("MENSUELLE"); // Set default type or derive from API response
                } else {
                    logger.warn("Tarification for project {} returned null.", projet.getId());
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
}
