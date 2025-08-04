package org.egh.demo.controller;

import jakarta.persistence.EntityNotFoundException;
import org.egh.demo.dto.ProjetRequestDTO;
import org.egh.demo.entity.Projet;
import org.egh.demo.service.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projets")
public class ProjetController {

    @Autowired
    private ProjetService projetService;



    @PostMapping
    public ResponseEntity<Projet> createProjet(@RequestBody ProjetRequestDTO projetDTO) {
        try {
            Projet savedProjet = projetService.save(projetDTO);
            return ResponseEntity.ok(savedProjet);
        } catch (EntityNotFoundException e) {
            // Handle the case where the Devis or Utilisateur ID doesn't exist
            // Returning a 404 Not Found is appropriate here
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Projet>> getAllProjets() {
        return ResponseEntity.ok(projetService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projet> getProjetById(@PathVariable Long id) {
        Optional<Projet> projet = projetService.findById(id);
        return projet.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Projet> updateProjet(@PathVariable Long id, @RequestBody Projet projet) {
        Optional<Projet> updatedProjet = Optional.ofNullable(projetService.update(id, projet));
        return updatedProjet.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjet(@PathVariable Long id) {
        boolean deleted = projetService.delete(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
