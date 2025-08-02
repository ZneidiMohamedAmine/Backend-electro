package org.egh.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.egh.demo.entity.Status;
import org.egh.demo.entity.Travail;


@Getter
@Setter
@NoArgsConstructor
public class DevisDTO {
    private String description;
    private Travail type;
    private float montant;
    private Status status;
    private Long clientId;  // for user id
    // getters & setters
}

