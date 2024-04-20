package com.example.cryptoExchange.Exceptions;

import com.example.cryptoExchange.Exceptions.handlers.ErrorAttributesHandler;
import com.example.cryptoExchange.Exceptions.handlers.ErrorExchangeTransaction;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final ErrorAttributesHandler errorAttributesHandler;
    private final ErrorExchangeTransaction errorExchangeTransaction;

    public GlobalExceptionHandler(ErrorAttributesHandler errorAttributesHandler,
                                  ErrorExchangeTransaction errorExchangeTransaction) {
        this.errorAttributesHandler = errorAttributesHandler;
        this.errorExchangeTransaction = errorExchangeTransaction;
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(Model model, IllegalArgumentException e) {
        ModelAndView modelAndView = new ModelAndView();
        errorAttributesHandler.handleRegistrationError(model, e.getMessage());
        return modelAndView;
    }

    @ExceptionHandler
    public ModelAndView handleExchangeException(Model model, Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        errorExchangeTransaction.handleTransaction(model, e.getMessage());
        return modelAndView;
    }

}
