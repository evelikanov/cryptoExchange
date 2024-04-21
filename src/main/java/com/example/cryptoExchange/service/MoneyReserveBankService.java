package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.ReserveBank.MoneyReserveBank;

import java.math.BigDecimal;
import java.util.List;

public interface MoneyReserveBankService {
    List<MoneyReserveBank> updateBalancesInTransaction(String currencyToBuy, String currencyToSell, BigDecimal newMoneyReserveBankBalanceToBuy, BigDecimal newMoneyReserveBankBalanceToSell);
    BigDecimal getMoneyReserveBankBalanceById(String symbol);
    List<MoneyReserveBank> getAllMoneyReserveBanks();
    MoneyReserveBank updateMoneyReserveBankByCurrency(String symbol, BigDecimal balance);

    boolean isEnoughMoneyReserveBankBalance(String currencyToBuy, BigDecimal amount);
}
