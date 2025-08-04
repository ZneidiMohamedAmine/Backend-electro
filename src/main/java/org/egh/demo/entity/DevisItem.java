package org.egh.demo.entity;

import jakarta.persistence.*;

@Entity
public class DevisItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String designation;
    private int quantite;
    private float prixUnitaire;
    private float montant;
    private String description;

    @ManyToOne
    @JoinColumn(name = "devis_id")
    private Devis devis;
}
