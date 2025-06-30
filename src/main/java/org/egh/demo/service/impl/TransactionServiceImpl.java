package org.egh.demo.service.impl;

import org.egh.demo.entity.Transaction;
import org.egh.demo.repository.TransactionRepository;
import org.egh.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
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