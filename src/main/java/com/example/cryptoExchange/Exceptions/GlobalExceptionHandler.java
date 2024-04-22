package com.example.cryptoExchange.Exceptions;

import com.example.cryptoExchange.Exceptions.handlers.ErrorAttributesHandler;
import com.example.cryptoExchange.Exceptions.handlers.ErrorExchangeTransaction;
import com.example.cryptoExchange.Exceptions.handlers.ErrorSettingChange;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final ErrorAttributesHandler errorAttributesHandler;
    private final ErrorExchangeTransaction errorExchangeTransaction;
    private final ErrorSettingChange errorSettingChange;

    public GlobalExceptionHandler(ErrorAttributesHandler errorAttributesHandler,
                                  ErrorExchangeTransaction errorExchangeTransaction,
                                  ErrorSettingChange errorSettingChange) {
        this.errorAttributesHandler = errorAttributesHandler;
        this.errorExchangeTransaction = errorExchangeTransaction;
        this.errorSettingChange = errorSettingChange;
    }
    public ModelAndView handleRegistrationException(Model model, IllegalArgumentException e) {
        ModelAndView modelAndView = new ModelAndView();
        errorAttributesHandler.handleRegistrationError(model, e.getMessage());
        return modelAndView;
    }

    public ModelAndView handleExchangeException(Model model, Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        errorExchangeTransaction.handleTransaction(model, e.getMessage());
        return modelAndView;
    }
    public ModelAndView handleSettingException(Model model, Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        errorSettingChange.handleUserData(model, e.getMessage());
        return modelAndView;
    }

}
