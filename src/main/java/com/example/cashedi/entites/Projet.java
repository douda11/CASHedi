package com.example.cashedi.entites;

import com.example.cashedi.models.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "projets")
public class Projet {

    @Id
    private String id;
    private Long clientId;
    private String statut;
    private String etat;
    private Date dateEffet;
    private Date dateCreation;
    private Date dateModification;
    @JsonProperty("informations_resiliation_infra_annuelle")
    private InformationsResiliation resiliationInfraAnnuelle;
    private Assures assures;
    private String typeCotisation;
    private String commissionnement;
    private Offre offre;
    private Tarifs tarifs;
    private String codeAssociation;
    private int millesime;
    private String generationTarif;
    private Paiement paiement;
    private Remboursement remboursement;
    private List<Produit> produits;
    private Utilisateur utilisateur;
    private Archivage archivage;
    private Signature signature;
}
