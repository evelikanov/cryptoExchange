package com.example.cryptoExchange.repository;

import com.example.cryptoExchange.model.CryptoCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, Long> {
}