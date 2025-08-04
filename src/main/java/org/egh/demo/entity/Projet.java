package org.egh.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private StatusP status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date DateDebut;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date DateFin;

    private float cout;


    @OneToOne
    @JoinColumn(nullable=false)
    private Devis devis;

    @ManyToOne
    @JoinColumn(nullable=false)
    private Utilisateur utilisateur;

    @OneToMany(mappedBy="projet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    @OneToOne(mappedBy="projet", cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    private KPI kpi;

}
