package com.example.cryptoExchange.service.impl;

import com.example.cryptoExchange.model.Transaction.DepositTransaction;
import com.example.cryptoExchange.model.Transaction.Transaction;
import com.example.cryptoExchange.model.Transaction.WithdrawalTransaction;
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
    public void performMoneyDepositTransaction(Long userId, Long walletId, String currency, BigDecimal balance) {
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
    public void performMoneyWithdrawTransaction(Long userId, Long walletId, String currency, BigDecimal balance) {
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
    public void performCryptoDepositTransaction(Long userId, Long walletId, String cryptoCurrency, BigDecimal amount) {
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
    public void performCryptoWithdrawTransaction(Long userId, Long walletId, String cryptoCurrency, BigDecimal amount) {
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
    @Override
    public Transaction saveTransaction(Transaction Transaction) {
        return null;
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