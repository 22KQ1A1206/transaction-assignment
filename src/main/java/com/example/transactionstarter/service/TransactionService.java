package com.example.transactionstarter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.entity.TransactionStatus;
import com.example.transactionstarter.repository.TransactionRepository;

import com.example.transactionstarter.exception.DuplicateTransactionException;
import com.example.transactionstarter.exception.ResourceNotFoundException;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // 1. Create transaction
    public Transaction createTransaction(Transaction transaction) {
    	
    	if (transaction.getStatus() != TransactionStatus.PENDING) {
            throw new IllegalArgumentException(
                    "New transaction must have PENDING status");
        }

        if (transactionRepository.existsById(transaction.getTransactionId())) {
        	throw new DuplicateTransactionException("Transaction ID already exists");
        }

        return transactionRepository.save(transaction);
    }

    // 2. Get transaction
    public Transaction getTransaction(String transactionId) {

        return transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                new ResourceNotFoundException("Transaction not found"));
    }

    // 3. Update transaction status
    public Transaction updateTransactionStatus(
            String transactionId,
            TransactionStatus status) {

        Transaction transaction = getTransaction(transactionId);

        transaction.setStatus(status);

        return transactionRepository.save(transaction);
    }

    // 4. Get all transactions for a customer
    public List<Transaction> getCustomerTransactions(String customerId) {

        return transactionRepository.findByCustomerId(customerId);
    }
}