package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.ExchangeCurrency.CryptoCurrency;
import net.minidev.json.parser.ParseException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CryptoCurrencyService {
    CompletableFuture<Void> updateCryptoCurrencyRates() throws ParseException;
    CryptoCurrency saveCryptoCurrency(CryptoCurrency cryptoCurrency);
    List<CryptoCurrency> getAllCryptoCurrencies();
    CryptoCurrency getCryptoCurrencyById(Long id);
    void deleteCryptoCurrency(Long id);
}