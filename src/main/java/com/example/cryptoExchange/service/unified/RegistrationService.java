package com.example.cryptoExchange.service.unified;

import com.example.cryptoExchange.dto.RegistrationDTO;
import com.example.cryptoExchange.service.CryptoWalletService;
import com.example.cryptoExchange.service.MoneyWalletService;
import com.example.cryptoExchange.service.UserService;
import com.example.cryptoExchange.service.impl.UserServiceImpl;
import com.example.cryptoExchange.service.impl.WalletServiceImpl.CryptoWalletServiceImpl;
import com.example.cryptoExchange.service.impl.WalletServiceImpl.MoneyWalletServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {
    private final UserService userService;
    private final CryptoWalletService cryptoWalletService;
    private final MoneyWalletService moneyWalletService;

    public RegistrationService(UserService userService, CryptoWalletService cryptoWalletService, MoneyWalletService moneyWalletService) {
        this.userService = userService;
        this.cryptoWalletService = cryptoWalletService;
        this.moneyWalletService = moneyWalletService;
    }

    @Transactional
    public void perfomUserRegistration(RegistrationDTO registrationDTO) {
        userService.createUser(registrationDTO.getUsername(), registrationDTO.getPassword(),
                registrationDTO.getDateOfBirth(), registrationDTO.getEmail());
        cryptoWalletService.createCryptoWallet(registrationDTO.getUsername());
        moneyWalletService.createMoneyWallet(registrationDTO.getUsername());
    }
}
