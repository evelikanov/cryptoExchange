package com.example.cryptoExchange.Controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/data")
    public String data() {
        return "data";
    }

    @GetMapping("/wallet")
    public String wallet() {
        return "wallet";
    }

}
