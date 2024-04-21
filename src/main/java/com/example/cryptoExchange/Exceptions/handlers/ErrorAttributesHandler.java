package com.example.cryptoExchange.Exceptions.handlers;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import static com.example.cryptoExchange.constants.ErrorMessages.*;
import static com.example.cryptoExchange.constants.ViewAttribute.*;

@Component
public class ErrorAttributesHandler {
    public void handleRegistrationError(Model model, String errorMessage) {
        if (errorMessage.equals(USERNAME_TAKEN)) {
            model.addAttribute(USERNAME_MARK, errorMessage);
        } else if (errorMessage.equals(EMAIL_TAKEN)) {
            model.addAttribute(EMAIL_MARK, errorMessage);
        } else if (errorMessage.equals(FIELDS_NOT_FILLED)) {
            model.addAttribute(NULL_MARK, errorMessage);
        }
    }
}
