package com.example.cryptoExchange.service.unified;

import com.example.cryptoExchange.model.ExchangeCurrency.CryptoCurrency;
import com.example.cryptoExchange.model.ExchangeCurrency.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, Currency> redisCurrencyTemplate;
    @Autowired
    private RedisTemplate<String, CryptoCurrency> redisCryptoCurrencyTemplate;
    private static final String HASH_CURRENCY_KEY = "Currencies";
    private static final String HASH_CRYPTOCURRENCY_KEY = "CryptoCurrencies";

    public void saveCurrencyInCache(Currency currency) {
        Map<String, Currency> map = new HashMap<>();
        map.put(currency.getId().toString(), currency);
        redisCurrencyTemplate.opsForHash().putAll(HASH_CURRENCY_KEY, map);
    }
    public Currency getCurrencyFromCache(Long id) {
        return (Currency) redisCurrencyTemplate.opsForHash().get(HASH_CURRENCY_KEY, id.toString());
    }
    public void expireCurrencyCache() {
        redisCurrencyTemplate.expire(HASH_CURRENCY_KEY, 5, TimeUnit.MINUTES);
    }
    public List<Currency> getAllCurrenciesFromCache() {
        Map<Object, Object> currencyMap = redisCurrencyTemplate.opsForHash().entries(HASH_CURRENCY_KEY);
        return currencyMap.values().stream()
                .map(currency -> (Currency) currency)
                .collect(Collectors.toList());
    }
    public void saveCryptoCurrencyInCache(CryptoCurrency cryptoCurrency) {
        Map<String, CryptoCurrency> map = new HashMap<>();
        map.put(cryptoCurrency.getId().toString(), cryptoCurrency);
        redisCryptoCurrencyTemplate.opsForHash().putAll(HASH_CRYPTOCURRENCY_KEY, map);
    }
    public CryptoCurrency getCryptoCurrencyFromCache(Long id) {
        return (CryptoCurrency) redisCryptoCurrencyTemplate.opsForHash().get(HASH_CRYPTOCURRENCY_KEY, id.toString());
    }
    public void expireCryptoCurrencyCache() {
        redisCryptoCurrencyTemplate.expire(HASH_CRYPTOCURRENCY_KEY, 5, TimeUnit.MINUTES);
    }
    public List<CryptoCurrency> getAllCryptoCurrenciesFromCache() {
        Map<Object, Object> currencyMap = redisCryptoCurrencyTemplate.opsForHash().entries(HASH_CRYPTOCURRENCY_KEY);
        return currencyMap.values().stream()
                .map(cryptoCurrency -> (CryptoCurrency) cryptoCurrency)
                .collect(Collectors.toList());
    }

}
