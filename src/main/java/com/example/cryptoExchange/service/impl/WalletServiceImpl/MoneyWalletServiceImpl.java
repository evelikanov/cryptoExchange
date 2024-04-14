package com.example.cryptoExchange.service.impl.WalletServiceImpl;

import com.example.cryptoExchange.Exceptions.ErrorMessages;
import com.example.cryptoExchange.model.ExchangeCurrency.Currency;
import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.model.Wallet.MoneyWallet;
import com.example.cryptoExchange.repository.ExchangeCurrencyRepository.CurrencyRepository;
import com.example.cryptoExchange.repository.UserRepository;
import com.example.cryptoExchange.repository.WalletRepository.MoneyWalletRepository;
import com.example.cryptoExchange.service.MoneyWalletService;
import com.example.cryptoExchange.service.impl.ExchangeCurrencyServiceImpl.CurrencyServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MoneyWalletServiceImpl implements MoneyWalletService {
    @Autowired
    private User user;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MoneyWalletRepository moneyWalletRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private CurrencyServiceImpl currencyServiceImpl;


    @Transactional
    public void createMoneyWallet(String username) {
        User user = userRepository.findByUsername(username).orElse(null);

        List<MoneyWallet> wallets = new ArrayList<>();
        List<String> currencies = currencyServiceImpl.getAllCurrenciesSymbolList();
        for (String symbol : currencies) {
            Currency currency = currencyRepository.findBySymbol(symbol);
            if (currency != null) {
                MoneyWallet moneyWallet = new MoneyWallet();
                moneyWallet.setBalance(BigDecimal.ZERO);
                moneyWallet.setSymbol(symbol);
                moneyWallet.setCurrency(currency);
                moneyWallet.setUser(user);
                wallets.add(moneyWallet);
            }
        }
        moneyWalletRepository.saveAll(wallets);
    }

    public void isNegativeMoneyWalletField(BigDecimal balance) {
        if(balance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(ErrorMessages.NEGATIVE_NUMBER);
        }
    }


    //TODO подумать как объединить два кошелька в один сервис
    public void isEmptyMoneyWalletField(BigDecimal balance) {
        if(balance == null) {
            throw new IllegalArgumentException(ErrorMessages.AT_LEAST_ONE_FIELD);
        }
    }
    public List<MoneyWallet> getMoneyBalanceByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        List<MoneyWallet> moneyWallet = moneyWalletRepository.findByUserId(user.getId());
        moneyWallet.sort(Comparator.comparingLong(MoneyWallet::getId));
        return moneyWallet;
    }

    public MoneyWallet setNewMoneyBalance(String username, String currency, BigDecimal balance) {
        User user = userRepository.findByUsername(username).orElse(null);

        MoneyWallet moneyWallet = moneyWalletRepository.findByUserIdAndSymbol(user.getId(), currency);

        BigDecimal newBalance = moneyWallet.getBalance().add(balance);
        moneyWallet.setBalance(newBalance);
        moneyWalletRepository.save(moneyWallet);
        return moneyWallet;
    }
}
