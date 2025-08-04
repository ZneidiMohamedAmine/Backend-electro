package org.egh.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Devis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Add these fields to match frontend
    private String numero;                    // Quote number
    private LocalDateTime dateCreation;       // Creation date
    private LocalDateTime dateValidite;       // Validity date
    private float montantHT;                 // Amount without tax
    private float montantTTC;                // Amount with tax
    private float tva;                       // VAT rate
    private String conditions;                // Terms and conditions

    // Keep existing fields
    private String description;
    @Enumerated(EnumType.STRING)
    private Travail type;
    private float montant;                   // This might be redundant with montantTTC

    @Enumerated(EnumType.STRING)
    private Status status;                    // Rename to 'statut' to match frontend

    // Change this to match frontend relationship
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Utilisateur client;

    // Add this for quote items
    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DevisItem> items = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "devis")
    private Projet projet;
}
