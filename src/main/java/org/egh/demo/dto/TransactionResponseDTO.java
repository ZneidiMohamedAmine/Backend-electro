package org.egh.demo.dto;

import lombok.Getter;
import lombok.Setter;
import org.egh.demo.entity.StatusT;
import org.egh.demo.entity.Type;

import java.util.Date;

@Getter
@Setter
public class TransactionResponseDTO {
    private Long id;
    private Type type;
    private float montant;
    private Date date;
    private StatusT statut;
    private Long projetId;
    private String projetNom;
    private Long utilisateurId;
    private String utilisateurNom;
}
