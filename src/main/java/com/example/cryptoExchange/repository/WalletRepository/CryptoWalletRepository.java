package com.example.cryptoExchange.repository.WalletRepository;

import com.example.cryptoExchange.model.Wallet.CryptoWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CryptoWalletRepository extends JpaRepository<CryptoWallet, Long> {
    CryptoWallet save(CryptoWallet cryptoWallet);
    List<CryptoWallet> findByUserId(Long userId);
    CryptoWallet findByUserIdAndSymbol(Long userId, String Symbol);
}
