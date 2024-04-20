package com.example.cryptoExchange.repository.ExchangeCurrencyRepository;

import com.example.cryptoExchange.model.ExchangeCurrency.CryptoCurrency;
import jakarta.persistence.OrderBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrency, Long> {
    CryptoCurrency save(CryptoCurrency currency);
    CryptoCurrency findBySymbol(String name);
    @OrderBy("id")
    List<CryptoCurrency> findAll();
    @Query("SELECT c.symbol FROM CryptoCurrency c")
    List<String> findAllSymbols();

}