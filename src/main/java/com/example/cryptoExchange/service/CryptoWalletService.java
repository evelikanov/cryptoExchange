package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.Wallet.CryptoWallet;

public interface CryptoWalletService {
    CryptoWallet getCryptoBalanceByUsernameAndCurrency(String username, String currency);
    void createCryptoWallet(String username);
}
