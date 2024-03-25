package com.example.cryptoExchange.repository;

import com.example.cryptoExchange.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}