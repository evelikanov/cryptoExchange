package com.example.cryptoExchange.service.impl;

import com.example.cryptoExchange.constants.ErrorMessages;
import com.example.cryptoExchange.model.ReserveBank.MoneyReserveBank;
import com.example.cryptoExchange.repository.MoneyReserveBankRepository;
import com.example.cryptoExchange.service.MoneyReserveBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MoneyReserveBankServiceImpl implements MoneyReserveBankService {
    @Autowired
    private MoneyReserveBankRepository moneyReserveBankRepository;

    @Override
    public List<MoneyReserveBank> updateBalancesInTransaction(String currencyToBuy, String currencyToSell, BigDecimal newMoneyReserveBankBalanceToBuy, BigDecimal newMoneyReserveBankBalanceToSell) {

        MoneyReserveBank moneyReserveBankSell = moneyReserveBankRepository.findBySymbol(currencyToSell);
        MoneyReserveBank moneyReserveBankBuy = moneyReserveBankRepository.findBySymbol(currencyToBuy);

        moneyReserveBankSell.setBalance(newMoneyReserveBankBalanceToSell);
        moneyReserveBankBuy.setBalance(newMoneyReserveBankBalanceToBuy);

        return moneyReserveBankRepository.saveAll(List.of(moneyReserveBankSell, moneyReserveBankBuy));
    }
    @Override
    public BigDecimal getMoneyReserveBankBalanceById(String symbol) {
        BigDecimal moneyReserveBank = moneyReserveBankRepository.findBySymbol(symbol).getBalance();;
        return moneyReserveBank;
    }
    public boolean isEnoughMoneyReserveBankBalance(String currency, BigDecimal amount) {
        MoneyReserveBank moneyReserveBank = moneyReserveBankRepository.findBySymbol(currency);

        if (moneyReserveBank.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(ErrorMessages.INSUFFICIENT_RESERVE_BANK);
        }
        return true;
    }

    @Override
    public List<MoneyReserveBank> getAllMoneyReserveBanks() {
        return null;
    }

    @Override
    public MoneyReserveBank updateMoneyReserveBankByCurrency(String symbol, BigDecimal balance) {
        MoneyReserveBank moneyReserveBank = moneyReserveBankRepository.findBySymbol(symbol);
        moneyReserveBank.setBalance(balance);
        return moneyReserveBankRepository.save(moneyReserveBank);
    }
}
