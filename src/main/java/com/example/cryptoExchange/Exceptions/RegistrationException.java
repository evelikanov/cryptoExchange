package com.example.cryptoExchange.Exceptions;

import org.springframework.web.servlet.ModelAndView;
public class RegistrationException extends Exception {
    public RegistrationException (String message) {
        super(message);
    }

    public ModelAndView getModelAndView(IllegalArgumentException e) {
        ModelAndView errorModelAndView = new ModelAndView("/registration");

        if (e.getMessage().equals(ErrorMessages.USERNAME_TAKEN)) {
            errorModelAndView.addObject("usernameError", e.getMessage());
        } else if (e.getMessage().equals(ErrorMessages.EMAIL_TAKEN)) {
            errorModelAndView.addObject("emailError", e.getMessage());
        } else if (e.getMessage().equals(ErrorMessages.FIELDS_NOT_FILLED)) {
            errorModelAndView.addObject("nullError", e.getMessage());
        }
        return errorModelAndView;
    }
}
