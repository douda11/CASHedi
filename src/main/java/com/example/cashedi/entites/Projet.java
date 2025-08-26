package com.example.cashedi.entites;

import com.example.cashedi.models.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    @JsonProperty("client_id")
    private Long clientId;
    @JsonProperty("alptis_project_id")
    private String alptisProjectId;
    @JsonProperty("statut")
    private String statut;
    private String etat;
    @JsonProperty("date_effet")
    @NotNull(message = "La date d'effet ne peut pas être nulle")
    private Date dateEffet;
    private Date dateCreation;
    private Date dateModification;
    @JsonProperty("informations_resiliation_infra_annuelle")
    private InformationsResiliation resiliationInfraAnnuelle;
    @NotNull(message = "Les assurés ne peuvent pas être nuls")
    @Valid
    private Assures assures;
    private String typeCotisation;
    private String commissionnement;
    @NotNull(message = "L'offre ne peut pas être nulle")
    @Valid
    private Offre offre;
    private java.util.List<Combinaison> combinaisons;
    private Tarifs tarifs;
    private String codeAssociation;
    private int millesime;
    private String generationTarif;
    @NotNull(message = "Le paiement ne peut pas être nul")
    @Valid
    private Paiement paiement;
    private Remboursement remboursement;
    private List<Produit> produits;
    @NotNull(message = "L'utilisateur ne peut pas être nul")
    @Valid
    private Utilisateur utilisateur;
    private Archivage archivage;
    private Signature signature;
}
