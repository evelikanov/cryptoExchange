package com.example.cryptoExchange.service.impl.WalletServiceImpl;

import com.example.cryptoExchange.constants.ErrorMessages;
import com.example.cryptoExchange.model.ExchangeCurrency.CryptoCurrency;
import com.example.cryptoExchange.model.Wallet.CryptoWallet;
import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.repository.ExchangeCurrencyRepository.CryptoCurrencyRepository;
import com.example.cryptoExchange.repository.WalletRepository.CryptoWalletRepository;
import com.example.cryptoExchange.repository.UserRepository;
import com.example.cryptoExchange.service.CryptoWalletService;
import com.example.cryptoExchange.service.impl.ExchangeCurrencyServiceImpl.CryptoCurrencyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CryptoWalletServiceImpl implements CryptoWalletService {
    @Autowired
    private User user;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CryptoWalletRepository cryptoWalletRepository;
    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;
    @Autowired
    private CryptoCurrencyServiceImpl cryptoCurrencyServiceImpl;

    //Registration
    public void createCryptoWallet(String username) {
        User user = userRepository.findByUsername(username).orElse(null);

        List<CryptoWallet> wallets = new ArrayList<>();
        List<String> cryptoCurrencies = cryptoCurrencyServiceImpl.getAllCryptoCurrenciesSymbolList();
        for (String symbol : cryptoCurrencies) {
            CryptoCurrency cryptoCurrency = cryptoCurrencyRepository.findBySymbol(symbol);
            if (cryptoCurrency != null) {
                CryptoWallet cryptoWallet = new CryptoWallet();
                cryptoWallet.setAmount(BigDecimal.ZERO);
                cryptoWallet.setSymbol(symbol);
                cryptoWallet.setCryptoCurrency(cryptoCurrency);
                cryptoWallet.setUser(user);
                wallets.add(cryptoWallet);
            }
        }
        cryptoWalletRepository.saveAll(wallets);
    }

    public void isNegativeCryptoWalletField(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(ErrorMessages.NEGATIVE_NUMBER);
        }
    }

    public void isEmptyCryptoWalletField(BigDecimal amount) {
        if(amount == null) {
            throw new IllegalArgumentException(ErrorMessages.AT_LEAST_ONE_FIELD);
        }
    }
    public List<CryptoWallet> getCryptoBalanceByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        List<CryptoWallet> cryptoWallet = cryptoWalletRepository.findByUserId(user.getId());
        cryptoWallet.sort(Comparator.comparingLong(CryptoWallet::getId));
        return cryptoWallet;
    }
    @Transactional
    public CryptoWallet getCryptoBalanceByUsernameAndCurrency(String username, String cryptoCurrency) {
        User user = userRepository.findByUsername(username).orElse(null);
        CryptoWallet cryptoWallet = cryptoWalletRepository.findByUserIdAndSymbol(user.getId(), cryptoCurrency);
        return cryptoWallet;
    }

    public void topupCryptoBalance(String username, String cryptoCurrency, BigDecimal amount) {
        User user = userRepository.findByUsername(username).orElse(null);

        CryptoWallet cryptoWallet = cryptoWalletRepository.findByUserIdAndSymbol(user.getId(), cryptoCurrency);

        BigDecimal newCryptoBalance = cryptoWallet.getAmount().add(amount);
        cryptoWallet.setAmount(newCryptoBalance);
        cryptoWalletRepository.save(cryptoWallet);
    }
    public void withdrawCryptoBalance(String username, String cryptoCurrency, BigDecimal amount) {
        User user = userRepository.findByUsername(username).orElse(null);
        CryptoWallet cryptoWallet = cryptoWalletRepository.findByUserIdAndSymbol(user.getId(), cryptoCurrency);

        BigDecimal newCryptoBalance = cryptoWallet.getAmount().subtract(amount);
        cryptoWallet.setAmount(newCryptoBalance);
        cryptoWalletRepository.save(cryptoWallet);
    }
    public CryptoWallet isEnoughCryptoBalance(String username, String cryptoCurrency, BigDecimal amount) {
        User user = userRepository.findByUsername(username).orElse(null);
        CryptoWallet cryptoWallet = cryptoWalletRepository.findByUserIdAndSymbol(user.getId(), cryptoCurrency);

        if (cryptoWallet.getAmount().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(ErrorMessages.INSUFFICIENT_BALANCE);
        }
        return cryptoWallet;
    }
    public CryptoWallet updateCryptoWalletByCryptoCurrencyIdAndUser(String username, String cryptoCurrency, BigDecimal amount) {
        CryptoWallet cryptoWallet = getCryptoBalanceByUsernameAndCurrency(username, cryptoCurrency);
        cryptoWallet.setAmount(amount);
        return cryptoWalletRepository.save(cryptoWallet);
    }

}
