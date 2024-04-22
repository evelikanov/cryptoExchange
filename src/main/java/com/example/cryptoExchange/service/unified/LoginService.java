package com.example.cryptoExchange.service.unified;

import com.example.cryptoExchange.dto.LoginDTO;
import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;

import static com.example.cryptoExchange.constants.ViewAttribute.NULL_MARK;

@Service
public class LoginService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isUserAuthenticated(LoginDTO loginDTO) {
        Optional<User> user = userService.findUserByUsername(loginDTO.getUsername());
        return user.isPresent() && passwordEncoder.matches(loginDTO.getPassword(), user.get().getPassword());
    }
}
