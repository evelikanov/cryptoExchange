package com.example.cryptoExchange;

import com.example.cryptoExchange.Controllers.RegistrationController;
import com.example.cryptoExchange.Exceptions.GlobalExceptionHandler;
import com.example.cryptoExchange.dto.RegistrationDTO;
import com.example.cryptoExchange.service.CryptoWalletService;
import com.example.cryptoExchange.service.MoneyWalletService;
import com.example.cryptoExchange.service.UserService;
import com.example.cryptoExchange.service.unified.RegistrationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import static javax.management.Query.eq;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class YourTestClass {

    @Mock
    private UserService userService;

    @Mock
    private CryptoWalletService cryptoWalletService;

    @Mock
    private MoneyWalletService moneyWalletService;

    @Test
    public void testMockObjectsNotNull() {
        // Проверяем, что все моки не равны null
        assertNotNull(userService);
        assertNotNull(cryptoWalletService);
        assertNotNull(moneyWalletService);

        // Настройка моков
        doNothing().when(userService).validateRegistration(anyString(), anyString(), anyString(), anyString());
        doNothing().when(userService).createUser(anyString(), anyString(), anyString(), anyString());
        doNothing().when(cryptoWalletService).createCryptoWallet(anyString());
        doNothing().when(moneyWalletService).createMoneyWallet(anyString());

        // Ваша логика тестирования с моками
    }
    @Test
    public void testUserServiceIsNull() {
        // Убеждаемся, что userService равен null
        userService = null;
        assertNull(userService);

        // Ваша логика тестирования с проверкой на null для userService
    }

    @Test
    public void testMockObjectsNull() {
        // Проверяем, что какой-то из моков равен null
        assertNull(userService);
        assertNull(cryptoWalletService);
        assertNull(moneyWalletService);

        // Проверяем, что при равенстве null выбрасывается IllegalStateException
        assertThrows(IllegalStateException.class, () -> {
            // Ваш код, который должен выбросить IllegalStateException
            // Например:
            if (userService == null || cryptoWalletService == null || moneyWalletService == null) {
                throw new IllegalStateException("One of the mock objects (userService, cryptoWalletService, moneyWalletService) is null. Cannot proceed with the test.");
            }
        });
    }
}
