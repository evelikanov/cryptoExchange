package com.example.cryptoExchange.Controllers;


import com.example.cryptoExchange.model.ExchangeCurrency.CryptoCurrency;
import com.example.cryptoExchange.repository.ExchangeCurrencyRepository.CryptoCurrencyRepository;
import com.example.cryptoExchange.service.impl.ExchangeCurrencyServiceImpl.CryptoCurrencyServiceImpl;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@NoArgsConstructor
public class CryptoCurrencyController {

    @Autowired
    private CryptoCurrencyServiceImpl cryptoCurrencyServiceImpl;
    @Autowired
    private CryptoCurrencyRepository cryptoCurrencyRepository;

    @GetMapping("/cryptoCurrencyList")
    public ModelAndView cryptoCurrencyList(Model model) {
        List<CryptoCurrency> cryptoCurrencies = cryptoCurrencyServiceImpl.getAllCryptoCurrencies();
        model.addAttribute("cryptoCurrencies", cryptoCurrencies);
        return new ModelAndView("/cryptoCurrencyList");
    }
}
