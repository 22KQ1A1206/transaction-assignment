package com.example.transactionstarter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.transactionstarter.entity.Transaction;
import com.example.transactionstarter.entity.TransactionStatus;
import com.example.transactionstarter.entity.TransactionType;
import com.example.transactionstarter.repository.TransactionRepository;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
    }

    // Test 1: Create transaction
    @Test
    void createTransaction_shouldCreateTransaction() throws Exception {

        String requestBody = """
                {
                    "transactionId": "TXN001",
                    "customerId": "CUS001",
                    "amount": 500.00,
                    "currency": "INR",
                    "transactionType": "PAYMENT",
                    "status": "PENDING"
                }
                """;

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("TXN001"))
                .andExpect(jsonPath("$.customerId").value("CUS001"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    // Test 2: Get transaction
    @Test
    void getTransaction_shouldReturnTransaction() throws Exception {

        Transaction transaction = new Transaction();
        transaction.setTransactionId("TXN002");
        transaction.setCustomerId("CUS002");
        transaction.setAmount(new java.math.BigDecimal("1000.00"));
        transaction.setCurrency("INR");
        transaction.setTransactionType(TransactionType.PAYMENT);
        transaction.setStatus(TransactionStatus.PENDING);

        transactionRepository.save(transaction);

        mockMvc.perform(get("/api/transactions/TXN002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("TXN002"))
                .andExpect(jsonPath("$.customerId").value("CUS002"));
    }

    // Test 3: Update transaction status
    @Test
    void updateStatus_shouldUpdateTransaction() throws Exception {

        Transaction transaction = new Transaction();
        transaction.setTransactionId("TXN003");
        transaction.setCustomerId("CUS003");
        transaction.setAmount(new java.math.BigDecimal("750.00"));
        transaction.setCurrency("INR");
        transaction.setTransactionType(TransactionType.PAYMENT);
        transaction.setStatus(TransactionStatus.PENDING);

        transactionRepository.save(transaction);

        mockMvc.perform(patch("/api/transactions/TXN003/status")
                .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    // Test 4: Get customer transactions
    @Test
    void getCustomerTransactions_shouldReturnCustomerTransactions()
            throws Exception {

        Transaction transaction = new Transaction();
        transaction.setTransactionId("TXN004");
        transaction.setCustomerId("CUS004");
        transaction.setAmount(new java.math.BigDecimal("250.00"));
        transaction.setCurrency("INR");
        transaction.setTransactionType(TransactionType.PAYMENT);
        transaction.setStatus(TransactionStatus.PENDING);

        transactionRepository.save(transaction);

        mockMvc.perform(get("/api/transactions/customer/CUS004"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionId").value("TXN004"))
                .andExpect(jsonPath("$[0].customerId").value("CUS004"));
    }
}