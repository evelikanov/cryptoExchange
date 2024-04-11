package com.example.cryptoExchange.repository;

import com.example.cryptoExchange.model.CryptoCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, Long> {
    CryptoCurrency save(CryptoCurrency currency);
    CryptoCurrency findBySymbol(String name);
    List<CryptoCurrency> findAll();
}