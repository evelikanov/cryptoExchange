package com.example.cryptoExchange.Exceptions.handlers;

import com.example.cryptoExchange.constants.ErrorMessages;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import static com.example.cryptoExchange.constants.ErrorMessages.AT_LEAST_ONE_FIELD;
import static com.example.cryptoExchange.constants.ErrorMessages.EMAIL_TAKEN;
import static com.example.cryptoExchange.constants.ViewAttribute.*;

@Component
public class ErrorSettingChange {
    public void handleUserData(Model model, String e) {
        switch(e) {
            case AT_LEAST_ONE_FIELD:
                model.addAttribute(NULL_MARK, e);
                break;
            case EMAIL_TAKEN:
                model.addAttribute(EMAIL_MARK, e);
                break;
        }
    }
}
