package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.Wallet.MoneyWallet;

import java.math.BigDecimal;
import java.util.List;

public interface MoneyWalletService {
    List<MoneyWallet> updateBalancesInTransaction(String username, String currencyToBuy, String currencyToSell, BigDecimal newUserMoneyWalletBalanceToBuy, BigDecimal newUserMoneyWalletBalanceToSell);
    MoneyWallet getMoneyBalanceByUsernameAndCurrency(String username, String currency);
    void topupMoneyBalance(String username, String currency, BigDecimal balance);
    List<MoneyWallet> getMoneyBalanceByUsername(String username);

    void createMoneyWallet(String username);
}
