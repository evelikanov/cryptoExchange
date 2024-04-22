package com.example.cryptoExchange.service.unified;

import com.example.cryptoExchange.Exceptions.GlobalExceptionHandler;
import com.example.cryptoExchange.dto.UserDataDTO;
import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import static com.example.cryptoExchange.constants.UrlAddress._HOME;
import static com.example.cryptoExchange.constants.ViewAttribute.*;

@Service
public class UserDataService {
    private final UserService userService;
    private final GlobalExceptionHandler globalExceptionHandler;

    public UserDataService(UserService userService, GlobalExceptionHandler globalExceptionHandler) {
        this.userService = userService;
        this.globalExceptionHandler = globalExceptionHandler;
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
    @Transactional
    public void updateUserDetails(Model model, UserDataDTO userDataDTO) {
        try {
            userService.validateSettingChange(userDataDTO.getName(), userDataDTO.getSurname(), userDataDTO.getPhoneNumber(),
                    userDataDTO.getDateOfBirth(), userDataDTO.getEmail());
            userService.setUserDetails(userDataDTO.getUsername(), userDataDTO.getName(), userDataDTO.getSurname(),
                    userDataDTO.getPhoneNumber(), userDataDTO.getEmail(), userDataDTO.getDateOfBirth());
            model.addAttribute(SUCCESS, true);
        } catch (IllegalArgumentException e) {
            globalExceptionHandler.handleSettingException(model, e);
        }
    }
}
