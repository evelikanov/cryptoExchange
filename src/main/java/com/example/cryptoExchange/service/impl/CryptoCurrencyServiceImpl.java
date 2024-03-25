package com.example.cryptoExchange.service.impl;

import com.example.cryptoExchange.model.CryptoCurrency;
import com.example.cryptoExchange.repository.CryptoCurrencyRepository;
import com.example.cryptoExchange.service.CryptoCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {

    private final CryptoCurrencyRepository cryptoCurrencyRepository;

    @Autowired
    public CryptoCurrencyServiceImpl(CryptoCurrencyRepository cryptoCurrencyRepository) {
        this.cryptoCurrencyRepository = cryptoCurrencyRepository;
    }

    @Override
    public CryptoCurrency saveCryptoCurrency(CryptoCurrency cryptoCurrency) {
        return cryptoCurrencyRepository.save(cryptoCurrency);
    }

    @Override
    public List<CryptoCurrency> getAllCryptoCurrencies() {
        return cryptoCurrencyRepository.findAll();
    }

    @Override
    public CryptoCurrency getCryptoCurrencyById(Long id) {
        return cryptoCurrencyRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteCryptoCurrency(Long id) {
        cryptoCurrencyRepository.deleteById(id);
    }
}