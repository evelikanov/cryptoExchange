package com.example.cryptoExchange.repository;

import com.example.cryptoExchange.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}