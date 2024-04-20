package com.example.cryptoExchange.service.impl;

import com.example.cryptoExchange.model.Transaction.*;
import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.model.Wallet.Wallet;
import com.example.cryptoExchange.repository.TransactionRepository;
import com.example.cryptoExchange.repository.UserRepository;
import com.example.cryptoExchange.repository.WalletRepository.CryptoWalletRepository;
import com.example.cryptoExchange.repository.WalletRepository.MoneyWalletRepository;
import com.example.cryptoExchange.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MoneyWalletRepository moneyWalletRepository;
    @Autowired
    private CryptoWalletRepository cryptoWalletRepository;
    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    @Transactional
    public void saveMoneyDepositTransaction(Long userId, Long walletId, String currency, BigDecimal balance) {
        DepositTransaction depositTransaction = new DepositTransaction();
        depositTransaction.setTimestamp(LocalDateTime.now());
        depositTransaction.setQuantity(balance);
        depositTransaction.setExchangeCurrency(currency);

        User user = userRepository.findById(userId).orElseThrow();
        Wallet wallet = moneyWalletRepository.findById(walletId).orElseThrow();

        depositTransaction.setUser(user);
        depositTransaction.setWallet(wallet);

        transactionRepository.save(depositTransaction);
    }
    @Transactional
    public void saveMoneyWithdrawTransaction(Long userId, Long walletId, String currency, BigDecimal balance) {
        WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction();
        withdrawalTransaction.setTimestamp(LocalDateTime.now());
        withdrawalTransaction.setQuantity(balance);
        withdrawalTransaction.setExchangeCurrency(currency);

        User user = userRepository.findById(userId).orElseThrow();
        Wallet wallet = moneyWalletRepository.findById(walletId).orElseThrow();

        withdrawalTransaction.setUser(user);
        withdrawalTransaction.setWallet(wallet);

        transactionRepository.save(withdrawalTransaction);
    }
    @Transactional
    public void saveMoneyBuyTransaction(Long userId, Long walletId, String currencyToBuy, BigDecimal amount, BigDecimal exchangePrice, BigDecimal exchangeRate) {
        BuyTransaction buyTransaction = new BuyTransaction();
        buyTransaction.setTimestamp(LocalDateTime.now());
        buyTransaction.setQuantity(amount);
        buyTransaction.setExchangeCurrency(currencyToBuy);
        buyTransaction.setExchangePrice(exchangePrice);
        buyTransaction.setExchangeRate(exchangeRate);

        User user = userRepository.findById(userId).orElseThrow();
        Wallet wallet = moneyWalletRepository.findById(walletId).orElseThrow();

        buyTransaction.setUser(user);
        buyTransaction.setWallet(wallet);

        transactionRepository.save(buyTransaction);
    }
    @Transactional
    public void saveMoneySellTransaction(Long userId, Long walletId, String currencyToBuy, BigDecimal amount, BigDecimal exchangePrice, BigDecimal exchangeRate) {
        SellTransaction sellTransaction = new SellTransaction();
        sellTransaction.setTimestamp(LocalDateTime.now());
        sellTransaction.setQuantity(amount);
        sellTransaction.setExchangeCurrency(currencyToBuy);
        sellTransaction.setExchangePrice(exchangePrice);
        sellTransaction.setExchangeRate(exchangeRate);

        User user = userRepository.findById(userId).orElseThrow();
        Wallet wallet = moneyWalletRepository.findById(walletId).orElseThrow();

        sellTransaction.setUser(user);
        sellTransaction.setWallet(wallet);

        transactionRepository.save(sellTransaction);
    }
    @Transactional
    public void saveCryptoDepositTransaction(Long userId, Long walletId, String cryptoCurrency, BigDecimal amount) {
        DepositTransaction depositTransaction = new DepositTransaction();
        depositTransaction.setTimestamp(LocalDateTime.now());
        depositTransaction.setQuantity(amount);
        depositTransaction.setExchangeCurrency(cryptoCurrency);

        User user = userRepository.findById(userId).orElseThrow();
        Wallet wallet = cryptoWalletRepository.findById(walletId).orElseThrow();

        depositTransaction.setUser(user);
        depositTransaction.setWallet(wallet);

        transactionRepository.save(depositTransaction);
    }
    @Transactional
    public void saveCryptoWithdrawTransaction(Long userId, Long walletId, String cryptoCurrency, BigDecimal amount) {
        WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction();
        withdrawalTransaction.setTimestamp(LocalDateTime.now());
        withdrawalTransaction.setQuantity(amount);
        withdrawalTransaction.setExchangeCurrency(cryptoCurrency);

        User user = userRepository.findById(userId).orElseThrow();
        Wallet wallet = cryptoWalletRepository.findById(walletId).orElseThrow();

        withdrawalTransaction.setUser(user);
        withdrawalTransaction.setWallet(wallet);

        transactionRepository.save(withdrawalTransaction);
    }

    @Transactional
    public void saveCryptoBuyTransaction(Long userId, Long walletId, String cryptoCurrency, BigDecimal amount, BigDecimal exchangePrice, BigDecimal exchangeRate) {
        BuyTransaction buyTransaction = new BuyTransaction();
        buyTransaction.setTimestamp(LocalDateTime.now());
        buyTransaction.setQuantity(amount);
        buyTransaction.setExchangeCurrency(cryptoCurrency);
        buyTransaction.setExchangePrice(exchangePrice);
        buyTransaction.setExchangeRate(exchangeRate);

        User user = userRepository.findById(userId).orElseThrow();
        Wallet wallet = cryptoWalletRepository.findById(walletId).orElseThrow();

        buyTransaction.setUser(user);
        buyTransaction.setWallet(wallet);

        transactionRepository.save(buyTransaction);
    }
    @Transactional
    public void saveCryptoSellTransaction(Long userId, Long walletId, String cryptoCurrency, BigDecimal amount, BigDecimal exchangePrice, BigDecimal exchangeRate) {
        SellTransaction sellTransaction = new SellTransaction();
        sellTransaction.setTimestamp(LocalDateTime.now());
        sellTransaction.setQuantity(amount);
        sellTransaction.setExchangeCurrency(cryptoCurrency);
        sellTransaction.setExchangePrice(exchangePrice);
        sellTransaction.setExchangeRate(exchangeRate);

        User user = userRepository.findById(userId).orElseThrow();
        Wallet wallet = cryptoWalletRepository.findById(walletId).orElseThrow();

        sellTransaction.setUser(user);
        sellTransaction.setWallet(wallet);

        transactionRepository.save(sellTransaction);
    }
    @Override
    public List<Transaction> getTransactionsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        List<Transaction> transactions = transactionRepository.findByUserId(user.getId());
        transactions.sort(Comparator.comparingLong(Transaction::getId));
        return transactions;
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return null;
    }

    @Override
    public void deleteTransaction(Long id) {

    }
}