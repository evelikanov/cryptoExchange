package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.Transaction.Transaction;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    void saveMoneyBuyTransaction(String username, Long moneyWalletId, String currencyToBuy, BigDecimal amount, BigDecimal totalPriceRub, BigDecimal rate);
    void saveMoneySellTransaction(String username, Long moneyWalletId, String currencyToBuy, BigDecimal amount, BigDecimal totalPriceRub, BigDecimal price);
    void saveMoneyDepositTransaction(String username, Long moneyWalletId, String currency, BigDecimal balance);
    void saveMoneyWithdrawTransaction(String username, Long walletId, String currency, BigDecimal balance);
    void saveCryptoDepositTransaction(String username, Long cryptoWalletId, String cryptoCurrency, BigDecimal amount);
    void saveCryptoWithdrawTransaction(String username, Long walletId, String cryptoCurrency, BigDecimal amount);
    void saveCryptoBuyTransaction(String username, Long walletId, String cryptoCurrency, BigDecimal amount, BigDecimal exchangePrice, BigDecimal exchangeRate);
    void saveCryptoSellTransaction(String username, Long walletId, String cryptoCurrency, BigDecimal amount, BigDecimal exchangePrice, BigDecimal exchangeRate);
    List<Transaction> getTransactionsByUsername(String username);
    Transaction getTransactionById(Long id);
    void deleteTransaction(Long id);
}