package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.Exceptions.ErrorMessages;
import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.repository.UserRepository;
import com.example.cryptoExchange.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@RestController
public class LoginController {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = ErrorMessages.INVALID_USER_PASS, defaultValue = "false") boolean loginError) {
        return new ModelAndView("/login");
    }

    @PostMapping("/login")
    public ModelAndView authenticateUser(@RequestParam("username") String username,
                                         @RequestParam("password") String password) {

        Optional<User> user = userServiceImpl.findUserByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return new ModelAndView("redirect:/home");
        } else {
            return new ModelAndView("/login");
        }
    }
}
