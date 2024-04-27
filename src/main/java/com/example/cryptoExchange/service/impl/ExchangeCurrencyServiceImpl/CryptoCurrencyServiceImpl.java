package com.example.cryptoExchange.service.impl.ExchangeCurrencyServiceImpl;

import com.example.cryptoExchange.model.ExchangeCurrency.CryptoCurrency;
import com.example.cryptoExchange.model.ExchangeCurrency.Currency;
import com.example.cryptoExchange.repository.ExchangeCurrencyRepository.CryptoCurrencyRepository;
import com.example.cryptoExchange.repository.ExchangeCurrencyRepository.CurrencyRepository;
import com.example.cryptoExchange.service.CryptoCurrencyService;
import com.example.cryptoExchange.service.unified.RedisService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {
    private RedisService redisService;
    private RestTemplate restCryptoCurrencyTemplate;
    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private CryptoCurrency cryptoCurrency;
    @Autowired
    public CryptoCurrencyServiceImpl(RestTemplate restCryptoCurrencyTemplate, CryptoCurrencyRepository cryptoCurrencyRepository,
                                     RedisService redisService) {
        this.restCryptoCurrencyTemplate = restCryptoCurrencyTemplate;
        this.cryptoCurrencyRepository = cryptoCurrencyRepository;
        this.redisService = redisService;
    }

    //Rate update
    public CompletableFuture<Void> updateCryptoCurrencyRates() {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://api.coingecko.com/api/v3/simple/price?ids=";

//        List<CryptoCurrency> cryptoCurrencies = redisService.getAllCryptoCurrenciesFromCache();
//        if(cryptoCurrencies == null) {
//            cryptoCurrencies = getAllCryptoCurrencies();
//        }

        List<CryptoCurrency> cryptoCurrencies = getAllCryptoCurrencies();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (CryptoCurrency cryptoCurrency : cryptoCurrencies) {
            BigDecimal rateCache = null;
            try {
                rateCache = redisService.getCryptoCurrencyFromCache(cryptoCurrency.getId()).getRate();
                log.info("rateCache = " + rateCache);
            } catch (NullPointerException e) {
                log.error("Error retrieving rate from cache: " + e.getMessage());

            }
            BigDecimal finalRateCache = rateCache;

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                String currencyName = cryptoCurrency.getName().toLowerCase(Locale.ROOT);
                BigDecimal rate = null;

                if (finalRateCache == null) {
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
                    rate = new BigDecimal(currencyObject.get("usd").toString());

                    cryptoCurrency.setRate(rate);
                    cryptoCurrencyRepository.save(cryptoCurrency);
                    redisService.saveCryptoCurrencyInCache(cryptoCurrency);
                    redisService.expireCryptoCurrencyCache();
                }
            });
            futures.add(future);
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenApply((voidResult) -> null);
    }

    public List<String> getAllCryptoCurrenciesSymbolList() {
        return cryptoCurrencyRepository.findAllSymbols();
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