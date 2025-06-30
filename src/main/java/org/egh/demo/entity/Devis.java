package org.egh.demo.entity;

import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Devis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Travail type;
    private float montant;
    private Status status;

    @ManyToOne
    @JoinColumn(nullable=false)
    private Utilisateur utilisateur;

    @OneToOne(mappedBy="devis")
    private Projet projet;




}
