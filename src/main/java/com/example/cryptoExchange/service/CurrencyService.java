package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.ExchangeCurrency.Currency;

import java.util.List;

public interface CurrencyService {
    Currency saveCurrency(Currency currency);
    List<Currency> getAllCurrencies();
    Currency getCurrencyById(Long id);
    void deleteCurrency(Long id);
}
