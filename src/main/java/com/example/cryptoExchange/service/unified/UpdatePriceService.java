package com.example.cryptoExchange.service.unified;

import com.example.cryptoExchange.model.ExchangeCurrency.CryptoCurrency;
import com.example.cryptoExchange.model.ExchangeCurrency.Currency;
import com.example.cryptoExchange.service.CryptoCurrencyService;
import com.example.cryptoExchange.service.CurrencyService;
import net.minidev.json.parser.ParseException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.example.cryptoExchange.constants.ViewAttribute.CRYPTOCURRENCY_MARK;
import static com.example.cryptoExchange.constants.ViewAttribute.CURRENCY_MARK;

@Service
public class UpdatePriceService {
    private final CryptoCurrencyService cryptoCurrencyService;
    private final CurrencyService currencyService;
    private final RedisService redisService;
    public UpdatePriceService(CryptoCurrencyService cryptoCurrencyService, CurrencyService currencyService,
                              RedisService redisService) {
        this.cryptoCurrencyService = cryptoCurrencyService;
        this.currencyService = currencyService;
        this.redisService = redisService;
    }

    @Async
    @Scheduled(fixedDelay = 600000)
    @Transactional
    public ListenableFuture<Void> updateRates(Model model) {
        try {
            List<CryptoCurrency> cryptoCurrencies;
            List<Currency> currencies;

            currencies = redisService.getAllCurrenciesFromCache();
            cryptoCurrencies = redisService.getAllCryptoCurrenciesFromCache();

            if(currencies.isEmpty() || cryptoCurrencies.isEmpty()) {
                currencies = currencyService.getAllCurrencies();
                cryptoCurrencies = cryptoCurrencyService.getAllCryptoCurrencies();
            }

            CompletableFuture<Void> updateCryptoCurrenciesFuture = cryptoCurrencyService.updateCryptoCurrencyRates();
            CompletableFuture<Void> updateCurrenciesFuture = currencyService.updateCurrencyRates();

            CompletableFuture.allOf(updateCryptoCurrenciesFuture, updateCurrenciesFuture).join();

            model.addAttribute(CRYPTOCURRENCY_MARK, cryptoCurrencies)
                    .addAttribute(CURRENCY_MARK, currencies);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return new AsyncResult<>(null);
    }
}
