package org.egh.demo.controller;

import org.egh.demo.dto.DevisDTO;
import org.egh.demo.entity.Devis;
import org.egh.demo.entity.Utilisateur;
import org.egh.demo.repository.UtilisateurRepository;
import org.egh.demo.service.DevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devis")
public class DevisController {

    @Autowired
    private DevisService devisService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @PostMapping
    public ResponseEntity<Devis> createDevis(@RequestBody DevisDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Utilisateur not found"));

        Devis devis = new Devis();
        devis.setDescription(dto.getDescription());
        devis.setType(dto.getType());
        devis.setMontant(dto.getMontant());
        devis.setStatus(dto.getStatus());
        devis.setUtilisateur(utilisateur);

        Devis saved = devisService.save(devis);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Devis>> getAllDevis() {
        return ResponseEntity.ok(devisService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Devis> getDevisById(@PathVariable Long id) {
        return ResponseEntity.ok(devisService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Devis> updateDevis(@PathVariable Long id, @RequestBody Devis devis) {
        return ResponseEntity.ok(devisService.update(id, devis));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevis(@PathVariable Long id) {
        devisService.delete(id);
        return ResponseEntity.ok().build();
    }
} 