package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.Exceptions.GlobalExceptionHandler;
import com.example.cryptoExchange.dto.RegistrationDTO;
import com.example.cryptoExchange.repository.ExchangeCurrencyRepository.CryptoCurrencyRepository;
import com.example.cryptoExchange.service.impl.UserServiceImpl;
import com.example.cryptoExchange.service.unified.RegistrationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import static com.example.cryptoExchange.constants.UrlAddress.*;
import static com.example.cryptoExchange.constants.ViewAttribute.*;

@RestController
public class RegistrationController {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @GetMapping(_REGISTRATION)
    public ModelAndView registration() {
        return new ModelAndView(_REGISTRATION);
    }

    @PostMapping(_REGISTRATION)
    public ModelAndView registerUser(Model model,
                                     RegistrationDTO registrationDTO) {
        try {
            registrationService.perfomUserRegistration(registrationDTO);
        } catch (IllegalArgumentException e) {
            globalExceptionHandler.handleRegistrationException(model, e);
            return new ModelAndView(_REGISTRATION);
        }
        return new ModelAndView(new RedirectView(_HOME))
                .addObject(REGISTRATION_SUCCESS, true);
    }
}
