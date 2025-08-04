package org.egh.demo.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.egh.demo.dto.ProjetRequestDTO;
import org.egh.demo.entity.Devis;
import org.egh.demo.entity.Projet;
import org.egh.demo.entity.Utilisateur;
import org.egh.demo.repository.DevisRepository;
import org.egh.demo.repository.ProjetRepository;
import org.egh.demo.repository.UtilisateurRepository;
import org.egh.demo.service.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjetServiceImpl implements ProjetService {

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private DevisRepository devisRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public Projet save(ProjetRequestDTO projetDTO) {
        // Find the Devis by ID, or throw an exception if not found
        Devis devis = devisRepository.findById(projetDTO.getDevisId())
                .orElseThrow(() -> new EntityNotFoundException("Devis with ID " + projetDTO.getDevisId() + " not found."));

        // Find the Utilisateur by ID, or throw an exception if not found
        Utilisateur utilisateur = utilisateurRepository.findById(projetDTO.getUtilisateurId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur with ID " + projetDTO.getUtilisateurId() + " not found."));

        // Create and populate the Projet entity from the DTO
        Projet projet = new Projet();
        projet.setNom(projetDTO.getNom());
        projet.setStatus(projetDTO.getStatus());
        projet.setDateDebut(projetDTO.getDateDebut());
        projet.setDateFin(projetDTO.getDateFin());
        projet.setCout(projetDTO.getCout());

        // Set the found entities
        projet.setDevis(devis);
        projet.setUtilisateur(utilisateur);

        // Save the new Projet
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