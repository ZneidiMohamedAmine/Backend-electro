package org.egh.demo.service;

import jakarta.transaction.Transactional;
import org.egh.demo.dto.TransactionRequestDTO;
import org.egh.demo.dto.TransactionResponseDTO;
import org.egh.demo.entity.Transaction;
import java.util.List;

public interface TransactionService {
    Transaction createTransaction(TransactionRequestDTO transactionDTO);

    @Transactional
    List<TransactionResponseDTO> getAllTransactions();

    Transaction save(TransactionRequestDTO transactionDTO);
    List<Transaction> findAll();
    Transaction findById(Long id);
    Transaction update(Long id, Transaction transaction);
    void delete(Long id);
} 