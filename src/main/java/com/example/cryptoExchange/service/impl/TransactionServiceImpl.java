package com.example.cryptoExchange.service.impl;

import com.example.cryptoExchange.model.Transaction.Transaction;
import com.example.cryptoExchange.repository.TransactionRepository;
import com.example.cryptoExchange.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private MoneyWalletRepository walletRepository;
//    @Autowired
//    private User user;
//    @Autowired
//    private Wallet wallet;


    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

//    @Transactional
//    public void performDepositTransaction(Transaction transaction, Long userId, Long walletId, BigDecimal amount, String type) {
//        transaction.setTimestamp(LocalDateTime.now());
//        transaction.setAmount(amount);
//        transaction.setType("DEPOSIT");
//
//        User user = userRepository.findById(userId).orElseThrow();
//        Wallet wallet = walletRepository.findById(walletId).orElseThrow();
//
//        transaction.getUsers().add(user);
//        transaction.getWallets().add(wallet);
//
//        transactionRepository.save(transaction);
//    }

    @Override
    public Transaction saveTransaction(Transaction Transaction) {
        return null;
    }

    @Override
    public List<Transaction> getTransactions() {
        return null;
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return null;
    }

    @Override
    public void deleteTransaction(Long id) {

    }
}