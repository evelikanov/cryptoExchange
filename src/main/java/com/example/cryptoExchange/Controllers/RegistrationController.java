package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.Exceptions.RegistrationException;
import com.example.cryptoExchange.repository.ExchangeCurrencyRepository.CryptoCurrencyRepository;
import com.example.cryptoExchange.repository.WalletRepository.MoneyWalletRepository;
import com.example.cryptoExchange.repository.UserRepository;
import com.example.cryptoExchange.service.impl.WalletServiceImpl.CryptoWalletServiceImpl;
import com.example.cryptoExchange.service.impl.WalletServiceImpl.MoneyWalletServiceImpl;
import com.example.cryptoExchange.service.impl.UserServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private MoneyWalletServiceImpl moneyWalletServiceImpl;
    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;
    @Autowired
    private MoneyWalletRepository moneyWalletRepository;
    @Autowired
    private CryptoWalletServiceImpl cryptoWalletServiceImpl;

    @GetMapping("/registration")
    public ModelAndView registration() {
        return new ModelAndView("/registration");
    }

    @PostMapping("/registration")
    public ModelAndView registerUser(@RequestParam("username") String username,
                                     @RequestParam("password") String password,
                                     @RequestParam("dateOfBirth") String dateOfBirth,
                                     @RequestParam("email") String email) {

        try {
            userServiceImpl.isNullRegistrationField(username, password, dateOfBirth, email);
            userServiceImpl.isExistedUser(username, email);
        } catch (IllegalArgumentException e) {
            RegistrationException registrationException = new RegistrationException(e.getMessage());
            ModelAndView errorModelAndView = registrationException.getModelAndView(e);
            return errorModelAndView;
        }
        userServiceImpl.createUser(username, password, dateOfBirth, email);
        cryptoWalletServiceImpl.createCryptoWallet(username);
        moneyWalletServiceImpl.createMoneyWallet(username);
        RedirectView redirectView = new RedirectView("/home");
        redirectView.addStaticAttribute("registrationSuccess", true);
        return new ModelAndView(redirectView);
    }
}
