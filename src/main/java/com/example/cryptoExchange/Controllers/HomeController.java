package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.model.CryptoCurrency;
import com.example.cryptoExchange.service.CryptoCurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {

    public final CryptoCurrencyService cryptoCurrencyService;
    @GetMapping("/home")
    public ModelAndView home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
            model.addAttribute("loggedIn", true);
        }
        return new ModelAndView("home");
    }
    @GetMapping("/cryptoCurrencyList")
    public ModelAndView cryptoCurrencyList(Model model) {
        // TODO: сделать вывод динамической информации из базы данных
        List<CryptoCurrency> cryptoCurrencies = cryptoCurrencyService.getAllCryptoCurrencies();
        model.addAttribute("cryptoCurrencies", cryptoCurrencies);
        return new ModelAndView("/cryptoCurrencyList");
    }
}
