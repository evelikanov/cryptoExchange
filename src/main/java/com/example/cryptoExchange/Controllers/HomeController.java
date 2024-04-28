package com.example.cryptoExchange.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

import static com.example.cryptoExchange.constants.UrlAddress.*;
import static com.example.cryptoExchange.constants.ViewAttribute.*;

@RestController
@RequiredArgsConstructor
public class HomeController {
    @GetMapping(_HOME)
    public ModelAndView home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute(LOGGED_USER, principal.getName())
                    .addAttribute(LOGGED_IN, true);
        }
        return new ModelAndView(_HOME);
    }
    @GetMapping(_ABOUTSERVICE)
    public ModelAndView aboutService() {
        return new ModelAndView(_ABOUTSERVICE);
    }

    @GetMapping(_COOPERATION)
    public ModelAndView feedBack() {
        return new ModelAndView(_COOPERATION);
    }
}
