package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.ExchangeCurrency.Currency;
import net.minidev.json.parser.ParseException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CurrencyService {
    Currency getCurrencyBySymbol(String symbol);
    CompletableFuture<Void> updateCurrencyRates() throws ParseException;
    Currency saveCurrency(Currency currency);
    List<Currency> getAllCurrencies();
    Currency getCurrencyById(Long id);
    void deleteCurrency(Long id);
    Long getCurrencyIdBySymbol(String symbol);
}
