package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.model.Wallet.CryptoWallet;
import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.model.Wallet.MoneyWallet;
import com.example.cryptoExchange.repository.CryptoWalletRepository;
import com.example.cryptoExchange.repository.TransactionRepository;
import com.example.cryptoExchange.repository.WalletRepository;
import com.example.cryptoExchange.service.impl.CryptoWalletServiceImpl;
import com.example.cryptoExchange.service.impl.TransactionServiceImpl;
import com.example.cryptoExchange.service.impl.UserServiceImpl;
import com.example.cryptoExchange.service.impl.WalletServiceImpl;
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
    private WalletServiceImpl walletServiceImpl;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private CryptoWalletServiceImpl cryptoWalletServiceImpl;
    @Autowired
    private CryptoWalletRepository cryptoWalletRepository;
    @Autowired
    private TransactionServiceImpl transactionServiceImpl;
    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/deals")
    public ModelAndView deals(Model model, Principal principal) {
        return new ModelAndView("/deals");
    }
    @GetMapping("/data")
    public ModelAndView data(Model model, Principal principal) {
        String username = principal.getName();
        User user = userServiceImpl.getDetailsByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("/data");
    }

    @DeleteMapping("/data")
    public ModelAndView deleteAccount(HttpSession session, Principal principal) {
        userServiceImpl.deleteUser(principal.getName());
        session.invalidate();
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/wallet")
    public ModelAndView walletGetBalance(Model model, Principal principal) {
        String username = principal.getName();

        MoneyWallet wallet = walletServiceImpl.getBalanceByUsername(username);
        CryptoWallet cryptoWallet = cryptoWalletServiceImpl.getCryptoBalanceByUsername(username);

        model.addAttribute("balance", wallet.getBalance());
        model.addAttribute("cryptoCurrency", cryptoWallet.getCryptoCurrency());
        model.addAttribute("amount", cryptoWallet.getAmount());
        return new ModelAndView("/wallet");
    }
    @PostMapping("/wallet")
    public ModelAndView walletPostBalance(Model model, Principal principal,
                               @RequestParam("balance") BigDecimal balance,
                               @RequestParam("cryptoCurrency") String cryptoCurrency,
                               @RequestParam("amount") BigDecimal amount) {
        String username = principal.getName();

        CryptoWallet cryptoWallet = cryptoWalletServiceImpl.setNewCryptoBalance(username, cryptoCurrency, amount);
        MoneyWallet wallet = walletServiceImpl.setNewBalance(username, balance);

        model.addAttribute("balance", wallet.getBalance());
        model.addAttribute("cryptoCurrency", cryptoWallet.getCryptoCurrency());
        model.addAttribute("amount", cryptoWallet.getAmount());
        return new ModelAndView("/wallet");
    }

    @GetMapping("/setting")
    public ModelAndView setting(Model model, Principal principal) {
        String username = principal.getName();
        User user = userServiceImpl.getDetailsByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("/setting");
    }
    @PutMapping("/setting")
    public ModelAndView settingChange(Model model, Principal principal,
                                      @RequestParam("name") String name,
                                      @RequestParam("surname") String surname,
                                      @RequestParam("phoneNumber") String phoneNumber,
                                      @RequestParam("email") String email,
                                      @RequestParam("dateOfBirth") String dateOfBirth) {
        ModelAndView modelAndView = new ModelAndView("/setting");
        String loggedUsername = principal.getName();

        if (name.isEmpty() && surname.isEmpty() && phoneNumber.isEmpty() && email.isEmpty() && dateOfBirth.isEmpty()) {
            modelAndView.addObject("nullError", "At least one field must be filled");
            User user = userServiceImpl.getDetailsByUsername(loggedUsername);
            model.addAttribute("user", user);
            return modelAndView;
        } else {
            try {
                User user = userServiceImpl.getDetailsByUsername(loggedUsername);
                Long newId = user.getId();
                userServiceImpl.updateUserDetails(newId, name, surname, phoneNumber, email, dateOfBirth);

                User userDetails = userServiceImpl.getDetailsByUsername(loggedUsername);
                User newUserDetails = userDetails;

                model.addAttribute("user", newUserDetails);
                return new ModelAndView("/setting");
            } catch (IllegalArgumentException e) {
                modelAndView.addObject("emailError", e.getMessage());
                User user = userServiceImpl.getDetailsByUsername(loggedUsername);
                model.addAttribute("user", user);
                return modelAndView;
            }
        }
    }
}
