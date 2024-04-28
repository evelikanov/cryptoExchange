package com.example.cryptoExchange.service.impl;

import com.example.cryptoExchange.constants.ErrorMessages;
import com.example.cryptoExchange.model.ReserveBank.MoneyReserveBank;
import com.example.cryptoExchange.repository.MoneyReserveBankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoneyReserveBankServiceImplTest {

    @Mock
    private MoneyReserveBankRepository moneyReserveBankRepository;

    @InjectMocks
    private MoneyReserveBankServiceImpl moneyReserveBankService;

    private MoneyReserveBank moneyReserveBankSell;
    private MoneyReserveBank moneyReserveBankBuy;

    @BeforeEach
    void setUp() {
        moneyReserveBankSell = new MoneyReserveBank();
        moneyReserveBankSell.setSymbol("USD");
        moneyReserveBankSell.setBalance(new BigDecimal("1000"));

        moneyReserveBankBuy = new MoneyReserveBank();
        moneyReserveBankBuy.setSymbol("EUR");
        moneyReserveBankBuy.setBalance(new BigDecimal("800"));
    }

    @Test
    @DisplayName("testUpdateBalancesInTransactionWhenValidParametersThenBalancesUpdated")
    void testUpdateBalancesInTransactionWhenValidParametersThenBalancesUpdated() {
        when(moneyReserveBankRepository.findBySymbol("USD")).thenReturn(moneyReserveBankSell);
        when(moneyReserveBankRepository.findBySymbol("EUR")).thenReturn(moneyReserveBankBuy);
        when(moneyReserveBankRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        BigDecimal newBalanceSell = new BigDecimal("900");
        BigDecimal newBalanceBuy = new BigDecimal("900");

        List<MoneyReserveBank> updatedBanks = moneyReserveBankService.updateBalancesInTransaction("EUR", "USD", newBalanceBuy, newBalanceSell);

        assertEquals(newBalanceSell, updatedBanks.get(0).getBalance());
        assertEquals(newBalanceBuy, updatedBanks.get(1).getBalance());
    }

    @Test
    @DisplayName("testGetMoneyReserveBankBalanceByIdWhenValidCurrencyThenCorrectBalanceReturned")
    void testGetMoneyReserveBankBalanceByIdWhenValidCurrencyThenCorrectBalanceReturned() {
        when(moneyReserveBankRepository.findBySymbol("USD")).thenReturn(moneyReserveBankSell);

        BigDecimal balance = moneyReserveBankService.getMoneyReserveBankBalanceById("USD");

        assertEquals(moneyReserveBankSell.getBalance(), balance);
    }

    @Test
    @DisplayName("testIsEnoughMoneyReserveBankBalanceWhenBalanceNotEnoughThenIllegalArgumentExceptionThrown")
    void testIsEnoughMoneyReserveBankBalanceWhenBalanceNotEnoughThenIllegalArgumentExceptionThrown() {
        when(moneyReserveBankRepository.findBySymbol("USD")).thenReturn(moneyReserveBankSell);

        BigDecimal amount = new BigDecimal("1100");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            moneyReserveBankService.isEnoughMoneyReserveBankBalance("USD", amount);
        });

        assertEquals(ErrorMessages.INSUFFICIENT_RESERVE_BANK, exception.getMessage());
    }

    @Test
    @DisplayName("testGetAllMoneyReserveBanksWhenCalledThenReturnListOfBanks")
    void testGetAllMoneyReserveBanksWhenCalledThenReturnListOfBanks() {
        // Настройка мока для возвращения предопределенного списка банков
        when(moneyReserveBankRepository.findAll()).thenReturn(List.of(moneyReserveBankSell, moneyReserveBankBuy));

        // Вызов метода
        List<MoneyReserveBank> banks = moneyReserveBankService.getAllMoneyReserveBanks();

        // Проверка результатов
        assertNotNull(banks);
        assertFalse(banks.isEmpty());
        assertEquals(2, banks.size());
    }

    @Test
    @DisplayName("testUpdateMoneyReserveBankByCurrencyWhenValidCurrencyAndBalanceThenBalanceUpdated")
    void testUpdateMoneyReserveBankByCurrencyWhenValidCurrencyAndBalanceThenBalanceUpdated() {
        when(moneyReserveBankRepository.findBySymbol("USD")).thenReturn(moneyReserveBankSell);
        when(moneyReserveBankRepository.save(any(MoneyReserveBank.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BigDecimal newBalance = new BigDecimal("1100");
        MoneyReserveBank updatedBank = moneyReserveBankService.updateMoneyReserveBankByCurrency("USD", newBalance);

        assertEquals(newBalance, updatedBank.getBalance());
    }
}
