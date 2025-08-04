package org.egh.demo.dto;


import lombok.Getter;
import lombok.Setter;
import org.egh.demo.entity.Status;
import org.egh.demo.entity.StatusP;
import org.egh.demo.entity.StatusT;
import org.egh.demo.entity.Type;

import java.util.Date;

@Getter
@Setter
public class TransactionRequestDTO {
    private Type type;
    private float montant;
    private Date date;
    private StatusT statut;
    private Long projetId;
    private Long utilisateurId;
}
