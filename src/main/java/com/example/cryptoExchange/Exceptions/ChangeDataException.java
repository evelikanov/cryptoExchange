package com.example.cryptoExchange.Exceptions;

import org.springframework.web.servlet.ModelAndView;

public class ChangeDataException extends Exception {
    public ChangeDataException (String message) {
        super(message);
    }

    public ModelAndView getModelAndView(IllegalArgumentException e) {
        ModelAndView errorModelAndView = new ModelAndView("/setting");

        if(e.getMessage().equals(ErrorMessages.AT_LEAST_ONE_FIELD)) {
            errorModelAndView.addObject("nullError", e.getMessage());
        } else if(e.getMessage().equals(ErrorMessages.EMAIL_TAKEN)) {
            errorModelAndView.addObject("emailError", e.getMessage());
        }
        return errorModelAndView;
    }
}
