package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public LoginController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @PostMapping("/login")
    public ModelAndView authenticateUser(@RequestParam("username") String username,
                                         @RequestParam("password") String password,
                                         Model model) {

        // TODO: сделать отображение информации о том, что мы залогинены или нет на главной странице
        boolean isAuthenticated = userServiceImpl.authenticateUser(username, password);
        if (isAuthenticated) {
            model.addAttribute("username", username);
            return new ModelAndView("redirect:/home");
        } else {
            model.addAttribute("message", "Invalid username or password");
            return new ModelAndView("redirect:/login");
        }
    }
}
