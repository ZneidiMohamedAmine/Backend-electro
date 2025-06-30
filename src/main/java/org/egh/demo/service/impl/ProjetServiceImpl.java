package org.egh.demo.service.impl;

import org.egh.demo.entity.Projet;
import org.egh.demo.repository.ProjetRepository;
import org.egh.demo.service.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetServiceImpl implements ProjetService {

    @Autowired
    private ProjetRepository projetRepository;

    @Override
    public Projet save(Projet projet) {
        return projetRepository.save(projet);
    }

    @Override
    public List<Projet> findAll() {
        return projetRepository.findAll();
    }

    @Override
    public Projet findById(Long id) {
        return projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet not found with id: " + id));
    }

    @Override
    public Projet update(Long id, Projet projet) {
        Projet existingProjet = findById(id);
        existingProjet.setNom(projet.getNom());
        existingProjet.setStatus(projet.getStatus());
        existingProjet.setDateDebut(projet.getDateDebut());
        existingProjet.setDateFin(projet.getDateFin());
        existingProjet.setCout(projet.getCout());
        existingProjet.setDevis(projet.getDevis());
        existingProjet.setUtilisateur(projet.getUtilisateur());
        return projetRepository.save(existingProjet);
    }

    @Override
    public void delete(Long id) {
        projetRepository.deleteById(id);
    }
} 