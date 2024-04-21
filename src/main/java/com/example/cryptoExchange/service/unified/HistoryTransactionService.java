package com.example.cryptoExchange.service.unified;

import com.example.cryptoExchange.model.Transaction.Transaction;
import com.example.cryptoExchange.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;

import static com.example.cryptoExchange.constants.ViewAttribute.TRANSACTION_MARK;

@Service
public class HistoryTransactionService {
    private final TransactionService transactionService;

    @Autowired
    public HistoryTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    public void getHistoryDeals(Model model, String username) {
        List<Transaction> transactions = transactionService.getTransactionsByUsername(username);
        model.addAttribute(TRANSACTION_MARK, transactions);
    }


}
