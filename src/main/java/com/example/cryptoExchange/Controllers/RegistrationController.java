package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

@RestController
public class RegistrationController {

    @GetMapping("/registration")
    public ModelAndView registration() {
        return new ModelAndView("registration");
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/registration")
    public ModelAndView registerUser(@RequestParam("username") String username,
                                     @RequestParam("password") String password,
                                     @RequestParam("dateOfBirth") String dateOfBirth,
                                     @RequestParam("email") String email) {
        ModelAndView modelAndView = new ModelAndView("/registration");

        if (userRepository.existsByUsername(username)) {
            modelAndView.addObject("usernameError", "Username has already been taken");
        } else if (userRepository.existsByEmail(email)) {
            modelAndView.addObject("emailError", "E-mail has already been taken");
        } else if (username.isEmpty() || password.isEmpty() || dateOfBirth.isEmpty() || email.isEmpty()) {
            modelAndView.addObject("nullError", "All fields must be filled");
        } else {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setDateOfBirth(dateOfBirth);
            user.setEmail(email);
            //TODO add entity WALLET (changeset)
            user.setBalance(BigDecimal.valueOf(0));
            user.setCurrency(BigDecimal.valueOf(0));

            userRepository.save(user);
            return new ModelAndView("redirect:/home");
        }

        return modelAndView;
    }

}
