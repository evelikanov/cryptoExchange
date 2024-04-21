package com.example.cryptoExchange.service.unified;

import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import static com.example.cryptoExchange.constants.UrlAddress._HOME;
import static com.example.cryptoExchange.constants.ViewAttribute.DELETE_SUCCESS;
import static com.example.cryptoExchange.constants.ViewAttribute.LOGGED_USER;

@Service
public class UserDataService {
    private final UserService userService;
    public UserDataService(UserService userService) {
        this.userService = userService;
    }
    public void getUserData(Model model, String username) {
        User user = userService.getDetailsByUsername(username);
        model.addAttribute(LOGGED_USER, user);
    }

    @Transactional
    public void deleteUserAccount(HttpSession session, String username) {
        userService.deleteAccountByUsername(username);
        session.invalidate();
    }

}
