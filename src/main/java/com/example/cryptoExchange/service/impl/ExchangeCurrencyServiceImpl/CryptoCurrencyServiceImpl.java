package com.example.cryptoExchange.service.impl.ExchangeCurrencyServiceImpl;

import com.example.cryptoExchange.model.ExchangeCurrency.CryptoCurrency;
import com.example.cryptoExchange.repository.ExchangeCurrencyRepository.CryptoCurrencyRepository;
import com.example.cryptoExchange.repository.ExchangeCurrencyRepository.CurrencyRepository;
import com.example.cryptoExchange.service.CryptoCurrencyService;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Service
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {
    private RestTemplate restTemplate;
    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private CryptoCurrency cryptoCurrency;

    public CryptoCurrencyServiceImpl() {

    }
    //Rate update
    public CompletableFuture<Void> updateCryptoCurrencyRates() {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://api.coingecko.com/api/v3/simple/price?ids=";

        List<CryptoCurrency> cryptoCurrencies = getAllCryptoCurrencies();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (CryptoCurrency cryptoCurrency : cryptoCurrencies) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                String currencyName = cryptoCurrency.getName().toLowerCase(Locale.ROOT);
                String url = apiUrl + currencyName + "&vs_currencies=usd";

                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
                String responseBody = response.getBody();

                JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = (JSONObject) parser.parse(responseBody);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                JSONObject currencyObject = (JSONObject) jsonObject.get(currencyName);
                BigDecimal rate = new BigDecimal(currencyObject.get("usd").toString());
                cryptoCurrency.setRate(rate);

                cryptoCurrencyRepository.save(cryptoCurrency);
            });

            futures.add(future);
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenApply((voidResult) -> null);
    }

    public List<String> getAllCryptoCurrenciesSymbolList() {
        return cryptoCurrencyRepository.findAllSymbols();
    }
    @Autowired
    public CryptoCurrencyServiceImpl(RestTemplate restTemplate, CryptoCurrencyRepository cryptoCurrencyRepository) {
        this.restTemplate = restTemplate;
        this.cryptoCurrencyRepository = cryptoCurrencyRepository;
    }
    public CryptoCurrency getCryptoCurrencyBySymbol(String symbol) {
        return cryptoCurrencyRepository.findBySymbol(symbol);
    }

    @Override
    public CryptoCurrency saveCryptoCurrency(CryptoCurrency cryptoCurrency) {
        return cryptoCurrencyRepository.save(cryptoCurrency);
    }
    @Override
    public List<CryptoCurrency> getAllCryptoCurrencies() {
        return cryptoCurrencyRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
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