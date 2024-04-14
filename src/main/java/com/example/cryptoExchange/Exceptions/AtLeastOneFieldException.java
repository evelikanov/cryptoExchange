package com.example.cryptoExchange.Exceptions;

import org.springframework.web.servlet.ModelAndView;

public class AtLeastOneFieldException extends Exception {
    public AtLeastOneFieldException (String message) {
        super(message);
    }
    public ModelAndView getModelAndView(IllegalArgumentException e) {
        ModelAndView errorModelAndView = new ModelAndView("/wallet");

        if(e.getMessage().equals(ErrorMessages.AT_LEAST_ONE_FIELD)) {
            errorModelAndView.addObject("nullError", e.getMessage());
        }
        return errorModelAndView;
    }
}
