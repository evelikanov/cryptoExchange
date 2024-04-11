package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.repository.CryptoCurrencyRepository;
import com.example.cryptoExchange.repository.UserRepository;
import com.example.cryptoExchange.repository.WalletRepository;
import com.example.cryptoExchange.service.impl.CryptoWalletServiceImpl;
import com.example.cryptoExchange.service.impl.UserServiceImpl;
import com.example.cryptoExchange.service.impl.WalletServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private WalletServiceImpl walletServiceImpl;
    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private CryptoWalletServiceImpl cryptoWalletServiceImpl;

    @GetMapping("/registration")
    public ModelAndView registration() {
        return new ModelAndView("registration");
    }

    @PostMapping("/registration")
    public ModelAndView registerUser(@RequestParam("username") String username,
                                     @RequestParam("password") String password,
                                     @RequestParam("dateOfBirth") String dateOfBirth,
                                     @RequestParam("email") String email) {
        ModelAndView modelAndView = new ModelAndView("/registration");

        if(username.isEmpty() || password.isEmpty() || dateOfBirth.isEmpty() || email.isEmpty()) {
            modelAndView.addObject("nullError", "All fields must be filled");
        } else {
            try {
                userServiceImpl.createUser(username, password, dateOfBirth, email);
                cryptoWalletServiceImpl.createCryptoWallet(username);
                walletServiceImpl.createWallet(username);
                return new ModelAndView("redirect:/home");
            } catch (IllegalArgumentException e) {
                ModelAndView errorModelAndView = new ModelAndView("/registration");
                if (e.getMessage().equals("Username has already been taken")) {
                    errorModelAndView.addObject("usernameError", e.getMessage());
                } else if (e.getMessage().equals("E-mail has already been taken")) {
                    errorModelAndView.addObject("emailError", e.getMessage());
                }
                return errorModelAndView;
            }
        }
        return modelAndView;
    }
}
