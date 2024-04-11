package com.example.cryptoExchange.Controllers;


import com.example.cryptoExchange.model.CryptoCurrency;
import com.example.cryptoExchange.model.CryptoCurrencyTariffs;
import com.example.cryptoExchange.repository.CryptoCurrencyRepository;
import com.example.cryptoExchange.service.impl.CryptoCurrencyServiceImpl;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
