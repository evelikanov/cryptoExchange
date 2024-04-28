package com.example.cryptoExchange.Exceptions.handlers;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import static com.example.cryptoExchange.constants.ErrorMessages.*;
import static com.example.cryptoExchange.constants.ViewAttribute.*;

@Component
public class ErrorWithdrawalhandler {
    public void handleWithdrawalError(Model model, String e) {
        switch (e) {
            case NEGATIVE_NUMBER:
                model.addAttribute(WRONGNUMBER_MARK, e);
                break;
            case INSUFFICIENT_BALANCE:
                model.addAttribute(NOBALANCE_MARK, e);
                break;
        }
    }
}
