package com.example.cryptoExchange.service.impl;

import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.model.Wallet.MoneyWallet;
import com.example.cryptoExchange.repository.UserRepository;
import com.example.cryptoExchange.repository.WalletRepository;
import com.example.cryptoExchange.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private User user;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Transactional
    public void createWallet(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        MoneyWallet wallet = new MoneyWallet();
        wallet.setUser(user);
        wallet.setBalance(BigDecimal.valueOf(0));
        wallet.setCurrency("RUB");
        walletRepository.save(wallet);
    }
    public MoneyWallet getBalanceByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        MoneyWallet wallet = walletRepository.findByUserId(user.getId());
        return wallet;
    }

    public MoneyWallet setNewBalance(String username, BigDecimal balance) {
        User user = userRepository.findByUsername(username).orElse(null);
        MoneyWallet moneyWallet = walletRepository.findByUserId(user.getId());
        BigDecimal newBalance = moneyWallet.getBalance().add(balance);
        moneyWallet.setBalance(newBalance);
        walletRepository.save(moneyWallet);
        return moneyWallet;
    }
}
