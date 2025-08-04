package org.egh.demo.service;

import org.egh.demo.dto.ProjetRequestDTO;
import org.egh.demo.entity.Projet;
import java.util.List;
import java.util.Optional;

public interface ProjetService {
    Projet save(ProjetRequestDTO projetDTO);
    List<Projet> findAll();
    Optional<Projet> findById(Long id);
    Projet update(Long id, Projet projet);
    boolean delete(Long id);
} 