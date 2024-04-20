package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.ReserveBank.MoneyReserveBank;

import java.math.BigDecimal;
import java.util.List;

public interface MoneyReserveBankService {
    List<MoneyReserveBank> updateBalancesInTransaction(Long currencyToBuyId, Long currencyToSellId, BigDecimal newMoneyReserveBankBalanceToBuy, BigDecimal newMoneyReserveBankBalanceToSell);
    BigDecimal getMoneyReserveBankBalanceById(Long id);
    List<MoneyReserveBank> getAllMoneyReserveBanks();
    MoneyReserveBank updateMoneyReserveBankByCurrencyId(Long id, BigDecimal balance);

    boolean isEnoughMoneyReserveBankBalance(String currencyToBuy, BigDecimal amount);
    boolean isNegativeMoneyReserveBankField(BigDecimal amount);
}
