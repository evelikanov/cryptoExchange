package com.example.cryptoExchange.repository.ExchangeCurrencyRepository;

import com.example.cryptoExchange.model.ExchangeCurrency.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency save(Currency currency);
    Currency findBySymbol(String name);
    List<Currency> findAll();
    @Query("SELECT c.symbol FROM Currency c")
    List<String> findAllSymbols();

}
