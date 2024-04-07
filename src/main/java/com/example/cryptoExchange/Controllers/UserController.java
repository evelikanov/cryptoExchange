package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.repository.UserRepository;
import com.example.cryptoExchange.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/data")
    public ModelAndView data(Model model, Principal principal) {
        String username = principal.getName();
        User user = userServiceImpl.getDetailsByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("/data");
    }

    @PostMapping("/data")
    public ModelAndView deleteAccount(HttpSession session, Principal principal) {
        userServiceImpl.deleteUser(principal.getName());
        session.invalidate();
        return new ModelAndView("redirect:/home");
    }


    //TODO add entity WALLET (changeset)
    @GetMapping("/wallet")
    public ModelAndView walletGetBalance(Model model, Principal principal) {
        String username = principal.getName();
        User user = userServiceImpl.getBalanceByUsername(username);

        model.addAttribute("balance", user.getBalance());
        model.addAttribute("currency", user.getCurrency());
        return new ModelAndView("/wallet");
    }
    //TODO add entity WALLET (changeset)
    @PostMapping("/wallet")
    public ModelAndView walletPostBalance(Model model, Principal principal,
                               @RequestParam("balance") BigDecimal balance,
                               @RequestParam("currency") BigDecimal currency) {
        String username = principal.getName();

        User user = userServiceImpl.getBalanceByUsername(username);

        BigDecimal newBalance = user.getBalance().add(balance);
        BigDecimal newCurrency = user.getCurrency().add(currency);

        user.setBalance(newBalance);
        user.setCurrency(newCurrency);

        userRepository.save(user);

        model.addAttribute("balance", user.getBalance());
        model.addAttribute("currency", user.getCurrency());
        return new ModelAndView("/wallet");
    }

    //CHECK!!

    @GetMapping("/setting")
    public ModelAndView setting(Model model, Principal principal) {
        String username = principal.getName();
        User user = userServiceImpl.getDetailsByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("/setting");
    }

    //TODO add password changing (username setting)

    @PostMapping("/setting")
    public ModelAndView settingChange(Model model, Principal principal,
                                      @RequestParam("username") String username,
                                      @RequestParam("dateOfBirth") String dateOfBirth,
                                      @RequestParam("email") String email) {

        String loggedUsername = principal.getName();

        User user = userServiceImpl.getDetailsByUsername(loggedUsername);

        Long newId = user.getId();
        userServiceImpl.updateUserDetails(newId, email, dateOfBirth, username);

        User userDetails = userServiceImpl.getDetailsByUsername(username);
        User newUserDetails = userDetails;

        model.addAttribute("user", newUserDetails);
        return new ModelAndView("/setting");
    }

    //CHECK
}
