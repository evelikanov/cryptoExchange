package com.example.cryptoExchange.repository;

import com.example.cryptoExchange.model.Transaction.Transaction;
import com.example.cryptoExchange.model.Wallet.MoneyWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction save(Transaction transaction);
    List<Transaction> findByUserId(Long userId);
}