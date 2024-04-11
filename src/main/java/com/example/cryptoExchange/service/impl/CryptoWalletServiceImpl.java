package com.example.cryptoExchange.service.impl;

import com.example.cryptoExchange.model.Wallet.CryptoWallet;
import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.model.Wallet.MoneyWallet;
import com.example.cryptoExchange.repository.CryptoWalletRepository;
import com.example.cryptoExchange.repository.UserRepository;
import com.example.cryptoExchange.service.CryptoWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CryptoWalletServiceImpl implements CryptoWalletService {
    @Autowired
    private User user;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CryptoWalletRepository cryptoWalletRepository;

    @Transactional
    public void createCryptoWallet(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        CryptoWallet cryptoWallet = new CryptoWallet();
        cryptoWallet.setAmount(BigDecimal.valueOf(0));
        cryptoWallet.setCryptoCurrency("BTC");
        cryptoWallet.setUser(user);
        cryptoWalletRepository.save(cryptoWallet);
    }

    public CryptoWallet getCryptoBalanceByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        CryptoWallet cryptoWallet = cryptoWalletRepository.findByUserId(user.getId());
        return cryptoWallet;
    }
    public CryptoWallet setNewCryptoBalance(String username, String cryptoCurrency, BigDecimal amount) {
        User user = userRepository.findByUsername(username).orElse(null);
        CryptoWallet cryptoWallet = cryptoWalletRepository.findByUserId(user.getId());
        BigDecimal newCryptoBalance = cryptoWallet.getAmount().add(amount);
        cryptoWallet.setCryptoCurrency(cryptoCurrency);
        cryptoWallet.setAmount(newCryptoBalance);
        cryptoWalletRepository.save(cryptoWallet);
        return cryptoWallet;
    }

}
