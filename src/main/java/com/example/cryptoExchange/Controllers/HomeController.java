package com.example.cryptoExchange.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {
    @GetMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("home");
    }
    @GetMapping("/cryptoCurrencyList")
    public ModelAndView cryptoCurrencyList() {
        return new ModelAndView("cryptoCurrencyList");
    }
    @GetMapping("/auth")
    public ModelAndView auth() {
        return new ModelAndView("auth");
    }
}
