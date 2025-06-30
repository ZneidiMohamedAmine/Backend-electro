package org.egh.demo.service;

import org.egh.demo.entity.Devis;
import java.util.List;

public interface DevisService {
    Devis save(Devis devis);
    List<Devis> findAll();
    Devis findById(Long id);
    Devis update(Long id, Devis devis);
    void delete(Long id);
} 