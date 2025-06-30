package org.egh.demo.service;

import org.egh.demo.entity.Transaction;
import java.util.List;

public interface TransactionService {
    Transaction save(Transaction transaction);
    List<Transaction> findAll();
    Transaction findById(Long id);
    Transaction update(Long id, Transaction transaction);
    void delete(Long id);
} 