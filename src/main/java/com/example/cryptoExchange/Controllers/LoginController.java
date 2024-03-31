package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public LoginController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String authenticateUser(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   Model model) {
        boolean isAuthenticated = userServiceImpl.authenticateUser(username, password);
        if (isAuthenticated) {
            model.addAttribute("username", username);
            return "redirect:/home";
        } else {
            model.addAttribute("message" , "Invalid username or password");
            return "redirect:/login";
        }
    }
}
