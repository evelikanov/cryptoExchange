package com.example.cryptoExchange.repository;

import com.example.cryptoExchange.model.ReserveBank.CryptoReserveBank;
import com.example.cryptoExchange.model.ReserveBank.MoneyReserveBank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CryptoReserveBankRepository extends JpaRepository<CryptoReserveBank, Long> {
    Optional<CryptoReserveBank> findById(Long id);
    CryptoReserveBank findBySymbol(String symbol);
    CryptoReserveBank save(CryptoReserveBank cryptoReserveBank);
}
