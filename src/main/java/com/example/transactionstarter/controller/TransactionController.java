package com.example.transactionstarter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.entity.TransactionStatus;
import com.example.transactionstarter.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // 1. Create transaction
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
    		@jakarta.validation.Valid @RequestBody Transaction transaction) {

        Transaction createdTransaction =
                transactionService.createTransaction(transaction);

        return new ResponseEntity<>(
                createdTransaction,
                HttpStatus.CREATED);
    }

    // 2. Get transaction
    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(
            @PathVariable String transactionId) {

        Transaction transaction =
                transactionService.getTransaction(transactionId);

        return ResponseEntity.ok(transaction);
    }

    // 3. Update transaction status
    @PatchMapping("/{transactionId}/status")
    public ResponseEntity<Transaction> updateTransactionStatus(
            @PathVariable String transactionId,
            @RequestParam TransactionStatus status) {

        Transaction updatedTransaction =
                transactionService.updateTransactionStatus(
                        transactionId, status);

        return ResponseEntity.ok(updatedTransaction);
    }

    // 4. Get all transactions for a customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Transaction>> getCustomerTransactions(
            @PathVariable String customerId) {

        List<Transaction> transactions =
                transactionService.getCustomerTransactions(customerId);

        return ResponseEntity.ok(transactions);
    }
}