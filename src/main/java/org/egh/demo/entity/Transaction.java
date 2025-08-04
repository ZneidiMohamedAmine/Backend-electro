package org.egh.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Use your own Enum classes if they exist
    @Enumerated(EnumType.STRING)
    private Type type;

    private float montant;

    private Date date;

    @Enumerated(EnumType.STRING)
    private StatusT statusT;

    // A Many-to-One relationship with Projet.
    // The JoinColumn annotation specifies the foreign key column name.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projet_id", nullable = false)
    private Projet projet;

    // A Many-to-One relationship with Utilisateur.
    // Assuming a Transaction is always linked to a user.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

}
