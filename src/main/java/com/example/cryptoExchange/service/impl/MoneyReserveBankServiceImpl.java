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
    public List<MoneyReserveBank> updateBalancesInTransaction(Long currencyToBuyId, Long currencyToSellId, BigDecimal newMoneyReserveBankBalanceToBuy, BigDecimal newMoneyReserveBankBalanceToSell) {

        MoneyReserveBank moneyReserveBankSell = moneyReserveBankRepository.findById(currencyToSellId).get();
        MoneyReserveBank moneyReserveBankBuy = moneyReserveBankRepository.findById(currencyToBuyId).get();

        moneyReserveBankSell.setBalance(newMoneyReserveBankBalanceToSell);
        moneyReserveBankBuy.setBalance(newMoneyReserveBankBalanceToBuy);

        return moneyReserveBankRepository.saveAll(List.of(moneyReserveBankSell, moneyReserveBankBuy));
    }
    @Override
    public BigDecimal getMoneyReserveBankBalanceById(Long id) {
        BigDecimal moneyReserveBank = moneyReserveBankRepository.findById(id).get().getBalance();;
        return moneyReserveBank;
    }
    public boolean isNegativeMoneyReserveBankField(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(ErrorMessages.NEGATIVE_NUMBER);
        }
        return true;
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
    public MoneyReserveBank updateMoneyReserveBankByCurrencyId(Long id, BigDecimal balance) {
        MoneyReserveBank moneyReserveBank = moneyReserveBankRepository.findById(id).get();
        moneyReserveBank.setBalance(balance);
        return moneyReserveBankRepository.save(moneyReserveBank);
    }
}
