package org.egh.demo.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.egh.demo.dto.TransactionRequestDTO;
import org.egh.demo.dto.TransactionResponseDTO;
import org.egh.demo.entity.Projet;
import org.egh.demo.entity.Transaction;
import org.egh.demo.entity.Utilisateur;
import org.egh.demo.repository.ProjetRepository;
import org.egh.demo.repository.TransactionRepository;
import org.egh.demo.repository.UtilisateurRepository;
import org.egh.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public Transaction createTransaction(TransactionRequestDTO transactionDTO) {
        Projet projet = projetRepository.findById(transactionDTO.getProjetId())
                .orElseThrow(() -> new EntityNotFoundException("Projet not found with ID: " + transactionDTO.getProjetId()));

        Utilisateur utilisateur = utilisateurRepository.findById(transactionDTO.getUtilisateurId())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur not found with ID: " + transactionDTO.getUtilisateurId()));

        Transaction transaction = new Transaction();
        transaction.setType(transactionDTO.getType());
        transaction.setMontant(transactionDTO.getMontant());
        transaction.setDate(transactionDTO.getDate());
        transaction.setStatusT(transactionDTO.getStatut());
        transaction.setProjet(projet);
        transaction.setUtilisateur(utilisateur);

        return transactionRepository.save(transaction);
    }

    @Transactional
    @Override
    public List<TransactionResponseDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TransactionResponseDTO convertToDto(Transaction transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setMontant(transaction.getMontant());
        dto.setType(transaction.getType());
        dto.setDate(transaction.getDate());
        dto.setStatut(transaction.getStatusT());

        if (transaction.getProjet() != null) {
            dto.setProjetId(transaction.getProjet().getId());
            dto.setProjetNom(transaction.getProjet().getNom());
        }

        if (transaction.getUtilisateur() != null) {
            dto.setUtilisateurId(transaction.getUtilisateur().getId());
            dto.setUtilisateurNom(transaction.getUtilisateur().getNom());
        }

        return dto;
    }

    @Override
    public Transaction save(TransactionRequestDTO transactionDTO) {
        return null;
    }

    @Override
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }

    @Override
    public Transaction update(Long id, Transaction transaction) {
        Transaction existingTransaction = findById(id);
        existingTransaction.setMontant(transaction.getMontant());
        existingTransaction.setProjet(transaction.getProjet());
        return transactionRepository.save(existingTransaction);
    }

    @Override
    public void delete(Long id) {
        transactionRepository.deleteById(id);
    }
} 