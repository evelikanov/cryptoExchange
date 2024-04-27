package com.example.cryptoExchange.service.impl.ExchangeCurrencyServiceImpl;

import com.example.cryptoExchange.model.ExchangeCurrency.Currency;
import com.example.cryptoExchange.repository.ExchangeCurrencyRepository.CurrencyRepository;
import com.example.cryptoExchange.service.CurrencyService;
import com.example.cryptoExchange.service.unified.RedisService;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class CurrencyServiceImpl implements CurrencyService {
    private RedisService redisService;
    private RestTemplate restCurrencyTemplate;
    private CurrencyRepository currencyRepository;

    public List<String> getAllCurrenciesSymbolList() {
        return currencyRepository.findAllSymbols();
    }
    @Autowired
    public CurrencyServiceImpl(RestTemplate restCurrencyTemplate, RedisService redisService,
                               CurrencyRepository currencyRepository) {
        this.restCurrencyTemplate = restCurrencyTemplate;
        this.currencyRepository = currencyRepository;
        this.redisService = redisService;
    }
    //Rate update
    public CompletableFuture<Void> updateCurrencyRates() {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://api.coingecko.com/api/v3/simple/price?ids=";

//        List<Currency> currencies = redisService.getAllCurrenciesFromCache();
//        if(currencies == null) {
//            currencies = getAllCurrencies();
//        }
        //TODO настроить взятие данных сначала из кэша
        List<Currency> currencies = getAllCurrencies();
        currencies.sort(Comparator.comparing(Currency::getId));

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (Currency currency : currencies) {
            BigDecimal rateCache = null;
            try {
                rateCache = redisService.getCurrencyFromCache(currency.getId()).getRate();
                log.info("rateCache = " + rateCache);
            } catch (NullPointerException e) {
                log.error("Error retrieving rate from cache: " + e.getMessage());

            }
            BigDecimal finalRateCache = rateCache;

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                String currencySymbol = currency.getSymbol().toLowerCase(Locale.ROOT);
                String excludedCurrencySymbol = "rub";
                BigDecimal rate = null;
                if(finalRateCache == null) {
                    if (!currencySymbol.equals(excludedCurrencySymbol)) {
                        String url = apiUrl + currencySymbol + "&vs_currencies=rub";

                        log.info("Я пошел через обновление");
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
                        rate = BigDecimal.ONE;
                    }
                    currency.setRate(rate);
                    currencyRepository.save(currency);
                    redisService.saveCurrencyInCache(currency);
                    redisService.expireCurrencyCache();
                }
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
