package com.example.cryptoExchange.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/data")
    public ModelAndView data() {
        return new ModelAndView("data");
    }

    @GetMapping("/wallet")
    public ModelAndView wallet() {
        return new ModelAndView("wallet");
    }
}
