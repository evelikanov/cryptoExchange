package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.Transaction.Transaction;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    void saveMoneyBuyTransaction(String username, Long moneyWalletId, String currencyToBuy, BigDecimal amount, BigDecimal totalPriceRub, BigDecimal rate);
    void saveMoneySellTransaction(String username, Long moneyWalletId, String currencyToBuy, BigDecimal amount, BigDecimal totalPriceRub, BigDecimal price);
    void saveMoneyDepositTransaction(String username, Long moneyWalletId, String currency, BigDecimal balance);
    void saveCryptoDepositTransaction(String username, Long cryptoWalletId, String cryptoCurrency, BigDecimal amount);
    List<Transaction> getTransactionsByUsername(String username);
    Transaction getTransactionById(Long id);
    void deleteTransaction(Long id);
}