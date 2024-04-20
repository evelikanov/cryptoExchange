package com.example.cryptoExchange.service.impl.ExchangeCurrencyServiceImpl;

import com.example.cryptoExchange.model.ExchangeCurrency.Currency;
import com.example.cryptoExchange.repository.ExchangeCurrencyRepository.CurrencyRepository;
import com.example.cryptoExchange.service.CurrencyService;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

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
    //Rate update
    public CompletableFuture<Void> updateCurrencyRates() {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://api.coingecko.com/api/v3/simple/price?ids=";

        List<Currency> currencies = getAllCurrencies();
        currencies.sort(Comparator.comparing(Currency::getId));

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (Currency currency : currencies) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                String currencySymbol = currency.getSymbol().toLowerCase(Locale.ROOT);
                String excludedCurrencySymbol = "rub";

                BigDecimal rate = null;
                if (!currencySymbol.equals(excludedCurrencySymbol)) {
                    String url = apiUrl + currencySymbol + "&vs_currencies=rub";


                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
                    String responseBody = response.getBody();

                    JSONParser parser = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = (JSONObject) parser.parse(responseBody);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    JSONObject currencyObject = (JSONObject) jsonObject.get(currencySymbol);
                    rate = new BigDecimal(currencyObject.get("rub").toString());

                } else if (currencySymbol.equals(excludedCurrencySymbol)) {
                    rate = new BigDecimal(1);
                }
                currency.setRate(rate);
                currencyRepository.save(currency);
            });

            futures.add(future);
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenApply((voidResult) -> null);
    }
    public Currency getCurrenciesBySymbol(String symbol) {
        return currencyRepository.findBySymbol(symbol);
    }
    public Currency getCurrencyBySymbol(String symbol) {
        return currencyRepository.findBySymbol(symbol);
    }
    @Override
    public Currency saveCurrency(Currency currency) {
        return null;
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public Currency getCurrencyById(Long id) {
        return null;
    }

    @Override
    public void deleteCurrency(Long id) {

    }
    @Override
    public Long getCurrencyIdBySymbol(String symbol) {
        return currencyRepository.findBySymbol(symbol).getId();
    }
}
