package org.egh.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Enumerated(EnumType.STRING)
    private Travail type;
    private float montant;

    @Enumerated(EnumType.STRING)
    private Status status;

    
    @ManyToOne
    @JoinColumn(nullable=false)
    private Utilisateur utilisateur;

    @JsonIgnore
    @OneToOne(mappedBy="devis")
    private Projet projet;




}
