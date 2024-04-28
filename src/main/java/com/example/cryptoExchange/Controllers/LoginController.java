package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.dto.LoginDTO;
import com.example.cryptoExchange.service.unified.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import static com.example.cryptoExchange.constants.UrlAddress.*;
import static com.example.cryptoExchange.constants.ErrorMessages.*;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    @GetMapping(_LOGIN)
    public ModelAndView login(@RequestParam(value = INVALID_USER_PASS, defaultValue = "false") boolean loginError) {
        return new ModelAndView(_LOGIN);
    }

    @PostMapping(_LOGIN)
    public ModelAndView authenticateUser(LoginDTO loginDTO) {

        if (loginService.isUserAuthenticated(loginDTO)) {
            return new ModelAndView(_HOME);
        } else {
            return new ModelAndView(_LOGIN);
        }
    }
}
