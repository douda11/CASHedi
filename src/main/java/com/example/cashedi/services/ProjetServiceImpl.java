package com.example.cashedi.services;

import com.example.cashedi.entites.Projet;
import com.example.cashedi.models.Archivage;
import com.example.cashedi.models.Signature;
import com.example.cashedi.repositories.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ProjetServiceImpl implements ProjetService {

    @Autowired
    private ProjetRepository projetRepository;

    @Override
    public Projet createProject(Projet projetDetails) {
        Projet newProjet = new Projet();

        // Set server-managed fields
        newProjet.setDateCreation(new Date());
        newProjet.setStatut("NOUVEAU");
        newProjet.setEtat("EN_COURS");
        newProjet.setClientId(projetDetails.getClientId());

        // Copy all fields from the incoming request data
        newProjet.setDateEffet(projetDetails.getDateEffet());
        newProjet.setCommissionnement(projetDetails.getCommissionnement());
        newProjet.setOffre(projetDetails.getOffre());
        newProjet.setAssures(projetDetails.getAssures());
        newProjet.setPaiement(projetDetails.getPaiement());
        newProjet.setRemboursement(projetDetails.getRemboursement());
        newProjet.setResiliationInfraAnnuelle(projetDetails.getResiliationInfraAnnuelle());
        newProjet.setUtilisateur(projetDetails.getUtilisateur());
        newProjet.setTarifs(projetDetails.getTarifs());
        newProjet.setProduits(projetDetails.getProduits());
        newProjet.setSignature(new Signature());
        newProjet.setArchivage(new Archivage());
        newProjet.setTypeCotisation(projetDetails.getTypeCotisation());
        newProjet.setCodeAssociation(projetDetails.getCodeAssociation());
        newProjet.setMillesime(projetDetails.getMillesime());
        newProjet.setGenerationTarif(projetDetails.getGenerationTarif());

        return projetRepository.save(newProjet);
    }

    @Override
    public Optional<Projet> getProjectById(String projetId) {
        return projetRepository.findById(projetId);
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
