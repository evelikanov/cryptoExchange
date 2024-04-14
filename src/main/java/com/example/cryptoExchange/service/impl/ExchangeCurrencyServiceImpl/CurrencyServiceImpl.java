package com.example.cryptoExchange.service.impl.ExchangeCurrencyServiceImpl;

import com.example.cryptoExchange.model.ExchangeCurrency.Currency;
import com.example.cryptoExchange.repository.ExchangeCurrencyRepository.CurrencyRepository;
import com.example.cryptoExchange.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private RestTemplate restTemplate;
    private CurrencyRepository currencyRepository;

    public List<String> getAllCurrenciesSymbolList() {
        return currencyRepository.findAllSymbols();
    }
    @Autowired
    public CurrencyServiceImpl(RestTemplate restTemplate, CurrencyRepository currencyRepository) {
        this.restTemplate = restTemplate;
        this.currencyRepository = currencyRepository;
    }
    public Currency getCurrenciesBySymbol(String symbol) {
        return currencyRepository.findBySymbol(symbol);
    }

    @Override
    public Currency saveCurrency(Currency currency) {
        return null;
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return null;
    }

    @Override
    public Currency getCurrencyById(Long id) {
        return null;
    }

    @Override
    public void deleteCurrency(Long id) {

    }
}
