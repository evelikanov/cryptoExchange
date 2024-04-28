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
    void checkMoneyBalanceSufficiency(String username, String currency, BigDecimal balance);
    void withdrawMoneyBalance(String username, String cryptoCurrency, BigDecimal amount);
    MoneyWallet updateMoneyWalletByCurrencyAndUser(String username, String currency, BigDecimal balance);
}
