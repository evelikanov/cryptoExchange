package com.example.cryptoExchange.Exceptions.handlers;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import static com.example.cryptoExchange.constants.ErrorMessages.*;
import static com.example.cryptoExchange.constants.ViewAttribute.*;

@Component
public class ErrorAttributesHandler {
    public void handleRegistrationError(Model model, String e) {
        switch (e) {
            case USERNAME_TAKEN:
                model.addAttribute(USERNAME_MARK, e);
                break;
            case EMAIL_TAKEN:
                model.addAttribute(EMAIL_MARK, e);
                break;
            case FIELDS_NOT_FILLED:
                model.addAttribute(NULL_MARK, e);
                break;
        }
    }
}
