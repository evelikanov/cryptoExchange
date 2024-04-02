package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Optional;

@RestController
public class LoginController {
    private final UserServiceImpl userServiceImpl;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public LoginController(UserServiceImpl userServiceImpl, BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        //TODO Добавить выброс ошибки при неверном логине или пароле "BadCredentials" -> /login
        return new ModelAndView("login");
    }

    @PostMapping("/login")
    public ModelAndView authenticateUser(@RequestParam("username") String username,
                                         @RequestParam("password") String password,
                                         Model model) {

        Optional<User> user = userServiceImpl.findUserByUsername(username);

        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return new ModelAndView("redirect:/home");
        } else {
            return new ModelAndView("/login");
        }
    }
}
