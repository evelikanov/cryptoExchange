package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.Wallet.CryptoWallet;

import java.util.List;

public interface CryptoWalletService {
    CryptoWallet getCryptoBalanceByUsernameAndCurrency(String username, String currency);
    List<CryptoWallet> getCryptoBalanceByUsername(String username);
    void createCryptoWallet(String username);
}
