package com.example.cryptoExchange.Exceptions.handlers;


import com.example.cryptoExchange.constants.ErrorMessages;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import static com.example.cryptoExchange.constants.ErrorMessages.*;
import static com.example.cryptoExchange.constants.ViewAttribute.*;

@Component
public class ErrorExchangeTransaction {
    public void handleTransaction(Model model, String e) {
        switch (e) {
            case INSUFFICIENT_RESERVE_BANK:
                model.addAttribute(NORESERVE_MARK, e);
                break;
            case NEGATIVE_NUMBER:
                model.addAttribute(WRONGNUMBER_MARK, e);
                break;
        }
    }
}

