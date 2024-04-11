package com.example.cryptoExchange.service.impl;

import com.example.cryptoExchange.model.CryptoCurrency;
import com.example.cryptoExchange.model.CryptoCurrencyTariffs;
import com.example.cryptoExchange.repository.CryptoCurrencyRepository;
import com.example.cryptoExchange.service.CryptoCurrencyService;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CryptoCurrencyServiceImpl implements CryptoCurrencyService {
    private RestTemplate restTemplate;
    private CryptoCurrencyRepository cryptoCurrencyRepository;

    public CryptoCurrencyTariffs[] exchangeRateSell() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=10&page=1&sparkline=false";
        ResponseEntity<CryptoCurrencyTariffs[]> response = restTemplate.getForEntity(url, com.example.cryptoExchange.model.CryptoCurrencyTariffs[].class);
        com.example.cryptoExchange.model.CryptoCurrencyTariffs[] cryptoSell = response.getBody();

        String ids = Arrays.stream(cryptoSell).map(crypto -> crypto.getId()).collect(Collectors.joining(","));
        String priceUrl = "https://api.coingecko.com/api/v3/simple/price?ids=" + ids + "&vs_currencies=usd";
        ResponseEntity<String> priceResponse = restTemplate.getForEntity(priceUrl, String.class);
        JsonObject priceJson = new JsonParser().parse(priceResponse.getBody()).getAsJsonObject();

        for (com.example.cryptoExchange.model.CryptoCurrencyTariffs crypto : cryptoSell) {
            double price = priceJson.getAsJsonObject(crypto.getId()).get("usd").getAsDouble();
            double updatedPrice = price * 1.05;
            crypto.setPrice(updatedPrice);
        }
        return cryptoSell;
    }

    public CryptoCurrencyTariffs[] exchangeRateBuy() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=10&page=1&sparkline=false";
        ResponseEntity<CryptoCurrencyTariffs[]> response = restTemplate.getForEntity(url, com.example.cryptoExchange.model.CryptoCurrencyTariffs[].class);
        com.example.cryptoExchange.model.CryptoCurrencyTariffs[] cryptoBuy = response.getBody();

        String ids = Arrays.stream(cryptoBuy).map(crypto -> crypto.getId()).collect(Collectors.joining(","));
        String priceUrl = "https://api.coingecko.com/api/v3/simple/price?ids=" + ids + "&vs_currencies=usd";
        ResponseEntity<String> priceResponse = restTemplate.getForEntity(priceUrl, String.class);
        JsonObject priceJson = new JsonParser().parse(priceResponse.getBody()).getAsJsonObject();

        for (com.example.cryptoExchange.model.CryptoCurrencyTariffs crypto : cryptoBuy) {
            double price = priceJson.getAsJsonObject(crypto.getId()).get("usd").getAsDouble();
            double updatedPrice = price * 0.95;
            crypto.setPrice(updatedPrice);
        }
        return cryptoBuy;
    }

    @Autowired
    public CryptoCurrencyServiceImpl(RestTemplate restTemplate, CryptoCurrencyRepository cryptoCurrencyRepository) {
        this.restTemplate = restTemplate;
        this.cryptoCurrencyRepository = cryptoCurrencyRepository;
    }
    public CryptoCurrency getCryptoCurrenciesBySymbol(String symbol) {
        return cryptoCurrencyRepository.findBySymbol(symbol);
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