package com.example.cryptoExchange.repository;

import com.example.cryptoExchange.model.Wallet.MoneyWallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<MoneyWallet, Long> {
    MoneyWallet save(MoneyWallet wallet);
    MoneyWallet findByUserId(Long userId);
}