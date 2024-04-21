package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.ReserveBank.CryptoReserveBank;
import com.example.cryptoExchange.model.ReserveBank.MoneyReserveBank;

import java.math.BigDecimal;
import java.util.List;

public interface CryptoReserveBankService {
    BigDecimal getCryptoReserveBankBalanceBySymbol(String symbol);
    List<CryptoReserveBank> getAllCryptoReserveBanks();
    CryptoReserveBank saveCryptoReserveBank(CryptoReserveBank cryptoReserveBank);

    CryptoReserveBank updateCryptoReserveBankByCryptoCurrency(String symbol, BigDecimal amount);
}
