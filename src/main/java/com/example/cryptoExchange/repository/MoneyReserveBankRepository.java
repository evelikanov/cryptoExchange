package com.example.cryptoExchange.repository;

import com.example.cryptoExchange.model.ReserveBank.CryptoReserveBank;
import com.example.cryptoExchange.model.ReserveBank.MoneyReserveBank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MoneyReserveBankRepository extends JpaRepository<MoneyReserveBank, Long> {
    Optional<MoneyReserveBank> findById(Long id);
    MoneyReserveBank findBySymbol(String symbol);
    MoneyReserveBank save(MoneyReserveBank moneyReserveBank);
}
