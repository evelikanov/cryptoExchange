package com.example.cryptoExchange.repository;

import com.example.cryptoExchange.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}