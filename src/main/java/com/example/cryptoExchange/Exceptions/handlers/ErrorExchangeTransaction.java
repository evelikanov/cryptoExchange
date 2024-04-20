package com.example.cryptoExchange.Exceptions.handlers;


import com.example.cryptoExchange.constants.ErrorMessages;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import static com.example.cryptoExchange.constants.ErrorMessages.*;
import static com.example.cryptoExchange.constants.ViewAttribute.*;

@Component
public class ErrorExchangeTransaction {
    public void handleTransaction(Model model, String e) {
        if (e.equals(ErrorMessages.INSUFFICIENT_RESERVE_BANK)) {
            model.addAttribute(NORESERVE_MARK, ErrorMessages.INSUFFICIENT_RESERVE_BANK);
        } else if (e.equals(NEGATIVE_NUMBER)) {
            model.addAttribute(WRONGNUMBER_MARK, NEGATIVE_NUMBER);
        }
    }
}

