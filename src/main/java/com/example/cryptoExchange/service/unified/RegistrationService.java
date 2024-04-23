package com.example.cryptoExchange.service.unified;

import com.example.cryptoExchange.Exceptions.GlobalExceptionHandler;
import com.example.cryptoExchange.dto.RegistrationDTO;
import com.example.cryptoExchange.service.CryptoWalletService;
import com.example.cryptoExchange.service.MoneyWalletService;
import com.example.cryptoExchange.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import static com.example.cryptoExchange.constants.UrlAddress._REGISTRATION;

@Slf4j
@Service
public class RegistrationService {
    private final UserService userService;
    private final CryptoWalletService cryptoWalletService;
    private final MoneyWalletService moneyWalletService;
    private final GlobalExceptionHandler globalExceptionHandler;

    public RegistrationService(UserService userService, CryptoWalletService cryptoWalletService,
                               MoneyWalletService moneyWalletService, GlobalExceptionHandler globalExceptionHandler) {
        this.userService = userService;
        this.cryptoWalletService = cryptoWalletService;
        this.moneyWalletService = moneyWalletService;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @Transactional
    public void perfomUserRegistration(RegistrationDTO registrationDTO) {
        userService.validateRegistration(registrationDTO.getUsername(), registrationDTO.getPassword(),
                registrationDTO.getDateOfBirth(), registrationDTO.getEmail());
        userService.createUser(registrationDTO.getUsername(), registrationDTO.getPassword(),
                registrationDTO.getDateOfBirth(), registrationDTO.getEmail());
        cryptoWalletService.createCryptoWallet(registrationDTO.getUsername());
        moneyWalletService.createMoneyWallet(registrationDTO.getUsername());
        log.info("User {} registered successfully", registrationDTO.getUsername());
    }
}
