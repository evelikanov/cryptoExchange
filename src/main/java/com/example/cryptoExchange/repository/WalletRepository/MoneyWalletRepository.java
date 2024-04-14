package com.example.cryptoExchange.repository.WalletRepository;

import com.example.cryptoExchange.model.Wallet.MoneyWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MoneyWalletRepository extends JpaRepository<MoneyWallet, Long> {
    MoneyWallet save(MoneyWallet wallet);
    List<MoneyWallet> findByUserId(Long userId);
    MoneyWallet findByUserIdAndSymbol(Long userId, String Symbol);
}