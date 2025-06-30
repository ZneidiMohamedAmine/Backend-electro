package org.egh.demo.service;

import org.egh.demo.entity.Projet;
import java.util.List;

public interface ProjetService {
    Projet save(Projet projet);
    List<Projet> findAll();
    Projet findById(Long id);
    Projet update(Long id, Projet projet);
    void delete(Long id);
} 