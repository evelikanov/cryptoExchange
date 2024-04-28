package com.example.cryptoExchange.service.impl;

import com.example.cryptoExchange.model.ReserveBank.CryptoReserveBank;
import com.example.cryptoExchange.repository.CryptoReserveBankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CryptoReserveBankServiceImplTest {

    @Mock
    private CryptoReserveBankRepository cryptoReserveBankRepository;

    @InjectMocks
    private CryptoReserveBankServiceImpl cryptoReserveBankService;

    private CryptoReserveBank cryptoReserveBank;

    @BeforeEach
    void setUp() {
        cryptoReserveBank = new CryptoReserveBank();
        cryptoReserveBank.setId(1L);
        cryptoReserveBank.setSymbol("BTC");
        cryptoReserveBank.setAmount(new BigDecimal("100.00"));
    }

    @Test
    void testGetCryptoReserveBankBalanceBySymbolWhenSymbolExistsThenReturnBalance() {
        when(cryptoReserveBankRepository.findBySymbol("BTC")).thenReturn(cryptoReserveBank);
        BigDecimal balance = cryptoReserveBankService.getCryptoReserveBankBalanceBySymbol("BTC");
        assertEquals(new BigDecimal("100.00"), balance);
    }

    @Test
    void testGetCryptoReserveBankBalanceBySymbolWhenSymbolDoesNotExistThenThrowException() {
        when(cryptoReserveBankRepository.findBySymbol("ETH")).thenReturn(null);
        assertThrows(NullPointerException.class, () -> cryptoReserveBankService.getCryptoReserveBankBalanceBySymbol("ETH"));
    }

    @Test
    void testCheckCryptoReserveBankBalanceSufficiencyWhenBalanceIsSufficientThenNoException() {
        when(cryptoReserveBankRepository.findBySymbol("BTC")).thenReturn(cryptoReserveBank);
        assertDoesNotThrow(() -> cryptoReserveBankService.checkCryptoReserveBankBalanceSufficiency("BTC", new BigDecimal("50.00")));
    }

    @Test
    void testCheckCryptoReserveBankBalanceSufficiencyWhenBalanceIsInsufficientThenThrowException() {
        when(cryptoReserveBankRepository.findBySymbol("BTC")).thenReturn(cryptoReserveBank);
        assertThrows(IllegalArgumentException.class, () -> cryptoReserveBankService.checkCryptoReserveBankBalanceSufficiency("BTC", new BigDecimal("150.00")));
    }

    @Test
    void testGetAllCryptoReserveBanks() {
        when(cryptoReserveBankRepository.findAll()).thenReturn(Collections.singletonList(cryptoReserveBank));
        List<CryptoReserveBank> allCryptoReserveBanks = cryptoReserveBankService.getAllCryptoReserveBanks();
        assertFalse(allCryptoReserveBanks.isEmpty());
        assertEquals(1, allCryptoReserveBanks.size());
        assertEquals(cryptoReserveBank, allCryptoReserveBanks.get(0));
    }

    @Test
    void testSaveCryptoReserveBank() {
        when(cryptoReserveBankRepository.save(any(CryptoReserveBank.class))).thenReturn(cryptoReserveBank);
        CryptoReserveBank savedCryptoReserveBank = cryptoReserveBankService.saveCryptoReserveBank(cryptoReserveBank);
        assertNotNull(savedCryptoReserveBank);
        assertEquals(cryptoReserveBank.getId(), savedCryptoReserveBank.getId());
    }

    @Test
    void testUpdateCryptoReserveBankByCryptoCurrency() {
        when(cryptoReserveBankRepository.findBySymbol("BTC")).thenReturn(cryptoReserveBank);
        when(cryptoReserveBankRepository.save(any(CryptoReserveBank.class))).thenReturn(cryptoReserveBank);
        CryptoReserveBank updatedCryptoReserveBank = cryptoReserveBankService.updateCryptoReserveBankByCryptoCurrency("BTC", new BigDecimal("200.00"));
        assertNotNull(updatedCryptoReserveBank);
        assertEquals(new BigDecimal("200.00"), updatedCryptoReserveBank.getAmount());
    }
}
