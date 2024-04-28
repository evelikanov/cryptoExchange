package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.Wallet.CryptoWallet;

import java.math.BigDecimal;
import java.util.List;

public interface CryptoWalletService {
    CryptoWallet getCryptoBalanceByUsernameAndCurrency(String username, String cryptoCurrency);
    List<CryptoWallet> getCryptoBalanceByUsername(String username);
    void topupCryptoBalance(String username, String cryptoCurrency, BigDecimal amount);
    void createCryptoWallet(String username);
    void checkCryptoBalanceSufficiency(String username, String cryptoCurrency, BigDecimal amount);
    void withdrawCryptoBalance(String username, String cryptoCurrency, BigDecimal amount);
    CryptoWallet updateCryptoWalletByCryptoCurrencyAndUser(String username, String cryptoCurrency, BigDecimal amount);
}
