package com.example.cryptoExchange.repository;

import com.example.cryptoExchange.model.Wallet.CryptoWallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoWalletRepository extends JpaRepository<CryptoWallet, Long> {
    CryptoWallet save(CryptoWallet cryptoWallet);
    CryptoWallet findByUserId(Long userId);
}
