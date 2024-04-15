package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.ExchangeCurrency.CryptoCurrency;

import java.util.List;

public interface CryptoCurrencyService {
    CryptoCurrency saveCryptoCurrency(CryptoCurrency cryptoCurrency);
    List<CryptoCurrency> getAllCryptoCurrencies();
    CryptoCurrency getCryptoCurrencyById(Long id);
    void deleteCryptoCurrency(Long id);
}