package org.egh.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class KPI {
    @Id
    @GeneratedValue
    private Long id;
    private float chiffreAffaire;
    private float coutTotal;
    private float marge;
    private float tempsRealisation;

    @OneToOne
    @JoinColumn(nullable=false)
    private Projet projet;

}
