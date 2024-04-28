package com.example.cryptoExchange.service.impl;

import com.example.cryptoExchange.constants.ErrorMessages;
import com.example.cryptoExchange.model.ReserveBank.CryptoReserveBank;
import com.example.cryptoExchange.repository.CryptoReserveBankRepository;
import com.example.cryptoExchange.service.CryptoReserveBankService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CryptoReserveBankServiceImpl implements CryptoReserveBankService {
    @Autowired
    private CryptoReserveBankRepository cryptoReserveBankRepository;
    public BigDecimal getCryptoReserveBankBalanceBySymbol(String symbol) {
        BigDecimal cryptoReserveBank = cryptoReserveBankRepository.findBySymbol(symbol).getAmount();;
        return cryptoReserveBank;
    }
    public void checkCryptoReserveBankBalanceSufficiency(String cryptoCurrency, BigDecimal amount) {
        CryptoReserveBank cryptoReserveBank = cryptoReserveBankRepository.findBySymbol(cryptoCurrency);

        if (cryptoReserveBank.getAmount().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(ErrorMessages.INSUFFICIENT_RESERVE_BANK);
        }
    }

    @Override
    public List<CryptoReserveBank> getAllCryptoReserveBanks() {
        return null;
    }

    @Override
    public CryptoReserveBank saveCryptoReserveBank(CryptoReserveBank cryptoReserveBank) {
        return cryptoReserveBankRepository.save(cryptoReserveBank);
    }
    @Override
    public CryptoReserveBank updateCryptoReserveBankByCryptoCurrency(String symbol, BigDecimal amount) {
        CryptoReserveBank cryptoReserveBank = cryptoReserveBankRepository.findBySymbol(symbol);
        cryptoReserveBank.setAmount(amount);
        return cryptoReserveBankRepository.save(cryptoReserveBank);
    }
}
