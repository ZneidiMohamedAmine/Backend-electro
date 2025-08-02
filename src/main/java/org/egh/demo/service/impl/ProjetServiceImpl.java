package org.egh.demo.service.impl;

import org.egh.demo.entity.Projet;
import org.egh.demo.repository.ProjetRepository;
import org.egh.demo.service.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Optional<Projet> findById(Long id) {
        return Optional.ofNullable(projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet not found with id: " + id)));
    }

    @Override
    public Projet update(Long id, Projet projet) {
        Optional<Projet> existingProjetOptional = findById(id);
        if (existingProjetOptional.isEmpty()) {
            throw new RuntimeException("Projet not found with id: " + id);
        }

        Projet existingProjet = existingProjetOptional.get();
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
    public boolean delete(Long id) {
        projetRepository.deleteById(id);
        return false;
    }
} 