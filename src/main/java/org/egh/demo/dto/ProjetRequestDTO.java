package org.egh.demo.dto;

import lombok.Getter;
import lombok.Setter;
import org.egh.demo.entity.StatusP;

import java.util.Date;

@Getter
@Setter
public class ProjetRequestDTO {

    private String nom;
    private StatusP status;
    private Date DateDebut;
    private Date DateFin;
    private float cout;

    // This is the key change. Instead of the full Devis object,
    // we just accept the ID of the Devis.
    private Long devisId;

    // We also accept the ID of the Utilisateur
    private Long utilisateurId;


}
