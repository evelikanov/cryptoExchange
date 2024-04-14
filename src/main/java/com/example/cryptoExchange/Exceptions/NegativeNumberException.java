package com.example.cryptoExchange.Exceptions;

import org.springframework.web.servlet.ModelAndView;

public class NegativeNumberException extends Exception {
    public NegativeNumberException (String message) {
        super(message);
    }

    public ModelAndView getModelAndView(IllegalArgumentException e) {
        ModelAndView errorModelAndView = new ModelAndView("/wallet");

        if (e.getMessage().equals(ErrorMessages.NEGATIVE_NUMBER)) {
            errorModelAndView.addObject("numberError", e.getMessage());
        }
        return errorModelAndView;
    }
}
