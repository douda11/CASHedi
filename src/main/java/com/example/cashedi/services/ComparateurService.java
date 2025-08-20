package com.example.cashedi.services;

import com.example.cashedi.dto.ResultatComparaison;
import com.example.cashedi.entites.BesoinClient;
import com.example.cashedi.entites.Formule;
import com.example.cashedi.models.*;
import com.example.cashedi.repositories.IFormuleRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ComparateurService {

    private final IFormuleRepository formuleRepository;
    private final TarificationServiceUtwin tarificationServiceUtwin;

    public ComparateurService(IFormuleRepository formuleRepository, TarificationServiceUtwin tarificationServiceUtwin) {
        this.formuleRepository = formuleRepository;
        this.tarificationServiceUtwin = tarificationServiceUtwin;
    }

    public List<ResultatComparaison> comparerFormules(BesoinClient besoin) {

        // --- Étape 1: Récupérer les tarifs de l'API Utwin ---
        Map<String, Double> tarifsUtwin = new HashMap<>();
        try {
            TarificationRequestUtwin requestUtwin = mapToUtwinRequest(besoin);
            TarificationResponseUtwin responseUtwin = tarificationServiceUtwin.getTarifs(requestUtwin)
                    .block(Duration.ofSeconds(10)); // Appel bloquant

            if (responseUtwin != null && responseUtwin.getPropositions() != null) {
                responseUtwin.getPropositions().stream()
                    .filter(p -> p.getLibelleFormule() != null)
                    .forEach(p -> tarifsUtwin.put(p.getLibelleFormule(), p.getCotisationMensuelleEuros()));
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'appel à l'API Utwin: " + e.getMessage());
        }

        // --- Étape 2: Calculer les scores pour toutes les formules et enrichir avec les tarifs Utwin ---
        List<ResultatComparaison> resultatsFinaux = new ArrayList<>();
        List<Formule> formulesDB = formuleRepository.findAll();

        for (Formule f : formulesDB) {
            double score = calculerScore(f, besoin);
            String nomFormule = f.getFormule(); // Le nom de la formule est maintenant un String 
            ResultatComparaison resultat = new ResultatComparaison(f.getNomDeLOffre(), nomFormule, score, f);

            // Si l'assureur est Utwin et qu'un tarif existe pour cette formule, on l'ajoute.
            if ("utwin".equalsIgnoreCase(f.getNomDeLOffre()) && tarifsUtwin.containsKey(nomFormule)) {
                resultat.setTarifMensuel(tarifsUtwin.get(nomFormule));
            }
            resultatsFinaux.add(resultat);
        }

        // --- Étape 3: Regrouper par assureur en gardant la meilleure offre (score le plus haut) ---
        Map<String, ResultatComparaison> meilleureOffreParAssureur = new HashMap<>();
        for (ResultatComparaison resultat : resultatsFinaux) {
            String nomAssureur = resultat.getNomDeLOffre();
            if (!meilleureOffreParAssureur.containsKey(nomAssureur) || resultat.getScore() > meilleureOffreParAssureur.get(nomAssureur).getScore()) {
                meilleureOffreParAssureur.put(nomAssureur, resultat);
            }
        }

        // --- Étape 4: Trier les assureurs par score (le plus haut est le meilleur) et retourner le top 3 ---
        return meilleureOffreParAssureur.values().stream()
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(3)
                .collect(Collectors.toList());
    }

    private double calculerScore(Formule f, BesoinClient besoin) {
        double score = 0;
        score += calculerProximite(f.getHospitalisation(), besoin.getHospitalisation());
        score += calculerProximite(f.getDentaire(), besoin.getDentaire());
        score += calculerProximite(f.getForfaitOptique(), besoin.getForfaitOptique());
        score += calculerProximite(f.getHonoraires(), besoin.getHonoraires());
        score += calculerProximite(f.getChambreParticuliere(), besoin.getChambreParticuliere());
        score += calculerProximite(f.getOrthodontie(), besoin.getOrthodontie());
        score += calculerProximite(f.getForfaitDentaire(), besoin.getForfaitDentaire());
        return score;
    }

    private double calculerProximite(int valeurFormule, int valeurBesoin) {
        if (valeurBesoin == 0) {
            return valeurFormule == 0 ? 1 : 0;
        }
        double differenceAbsolue = Math.abs(valeurFormule - valeurBesoin);
        double toleranceMax = 0.5 * valeurBesoin;
        if (differenceAbsolue == 0) {
            return 1;
        } else if (differenceAbsolue <= toleranceMax) {
            return 1 - (differenceAbsolue / toleranceMax);
        } else {
            return Math.exp(-differenceAbsolue / (double) valeurBesoin);
        }
    }

    private TarificationRequestUtwin mapToUtwinRequest(BesoinClient besoin) {
        TarificationRequestUtwin request = new TarificationRequestUtwin();

        Souscripteur souscripteur = new Souscripteur();
        Adresse adresse = new Adresse();
        adresse.setCodePostal(besoin.getCodePostal());
        souscripteur.setAdresse(adresse);
        request.setSouscripteur(souscripteur);

        com.example.cashedi.models.Besoin besoinUtwin = new com.example.cashedi.models.Besoin();
        besoinUtwin.setDateEffet(besoin.getDateEffet());
        request.setBesoin(besoinUtwin);

        List<com.example.cashedi.models.Assure> assures = besoin.getAssures().stream().map(assureClient -> {
            com.example.cashedi.models.Assure assure = new com.example.cashedi.models.Assure();
            assure.setDateDeNaissance(assureClient.getDateDeNaissance());
            assure.setCodeTypeRole(assureClient.getRole());

            if ("TNS".equalsIgnoreCase(assureClient.getRegime())) {
                assure.setCodeRegimeObligatoire("SSI");
            } else {
                assure.setCodeRegimeObligatoire(assureClient.getRegime());
            }
            return assure;
        }).collect(Collectors.toList());
        request.setAssures(assures);

        return request;
    }
}
