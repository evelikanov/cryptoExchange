package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.Transaction;
import java.util.List;

public interface TransactionService {
    Transaction saveTransaction(Transaction transaction);
    List<Transaction> getAllTransactions();
    Transaction getTransactionById(Long id);
    void deleteTransaction(Long id);
}