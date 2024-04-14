package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.Transaction.Transaction;
import java.util.List;

public interface TransactionService {
    Transaction saveTransaction(Transaction buySellTransaction);
    List<Transaction> getTransactionsByUsername(String username);
    Transaction getTransactionById(Long id);
    void deleteTransaction(Long id);
}