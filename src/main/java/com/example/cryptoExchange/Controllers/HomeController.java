package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.model.CryptoCurrency;
import com.example.cryptoExchange.service.CryptoCurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {

    public final CryptoCurrencyService cryptoCurrencyService;
    @GetMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("home");
    }
    @GetMapping("/cryptoCurrencyList")
    public List<CryptoCurrency> cryptoCurrencyList() {
//        return new ModelAndView("cryptoCurrencyList");
        // TODO: сделать вывод динамической информации из базы данных
        return cryptoCurrencyService.getAllCryptoCurrencies();
    }
    @GetMapping("/auth")
    public ModelAndView auth() {
        return new ModelAndView("auth");
    }
}
