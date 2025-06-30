package org.egh.demo.service.impl;

import org.egh.demo.entity.Devis;
import org.egh.demo.repository.DevisRepository;
import org.egh.demo.service.DevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DevisServiceImpl implements DevisService {

    @Autowired
    private DevisRepository devisRepository;

    @Override
    public Devis save(Devis devis) {
        return devisRepository.save(devis);
    }

    @Override
    public List<Devis> findAll() {
        return devisRepository.findAll();
    }

    @Override
    public Devis findById(Long id) {
        return devisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Devis not found with id: " + id));
    }

    @Override
    public Devis update(Long id, Devis devis) {
        Devis existingDevis = findById(id);
        existingDevis.setMontant(devis.getMontant());
        existingDevis.setType(devis.getType());
        return devisRepository.save(existingDevis);
    }

    @Override
    public void delete(Long id) {
        devisRepository.deleteById(id);
    }
} 