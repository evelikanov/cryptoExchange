package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.Exceptions.ChangeDataException;
import com.example.cryptoExchange.Exceptions.ErrorMessages;
import com.example.cryptoExchange.Exceptions.NegativeNumberException;
import com.example.cryptoExchange.model.Transaction.Transaction;
import com.example.cryptoExchange.model.Wallet.CryptoWallet;
import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.model.Wallet.MoneyWallet;
import com.example.cryptoExchange.model.Wallet.Wallet;
import com.example.cryptoExchange.repository.WalletRepository.CryptoWalletRepository;
import com.example.cryptoExchange.repository.TransactionRepository;
import com.example.cryptoExchange.repository.WalletRepository.MoneyWalletRepository;
import com.example.cryptoExchange.service.impl.WalletServiceImpl.CryptoWalletServiceImpl;
import com.example.cryptoExchange.service.impl.TransactionServiceImpl;
import com.example.cryptoExchange.service.impl.UserServiceImpl;
import com.example.cryptoExchange.service.impl.WalletServiceImpl.MoneyWalletServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private MoneyWalletServiceImpl moneyWalletServiceImpl;
    @Autowired
    private MoneyWalletRepository moneyWalletRepository;
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
        String username = principal.getName();
        List<Transaction> transactions = transactionServiceImpl.getTransactionsByUsername(username);
        model.addAttribute("transactions", transactions);
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
        RedirectView redirectView = new RedirectView("/home");
        redirectView.addStaticAttribute("deleteSuccess", true);
        return new ModelAndView(redirectView);
    }

    @GetMapping("/wallet")
    public ModelAndView walletGetBalance(Model model, Principal principal) {
        String username = principal.getName();

        List<MoneyWallet> wallet = moneyWalletServiceImpl.getMoneyBalanceByUsername(username);
        List<CryptoWallet> cryptoWallets = cryptoWalletServiceImpl.getCryptoBalanceByUsername(username);


        model.addAttribute("moneyWallets", wallet);
        model.addAttribute("cryptoWallets", cryptoWallets);
        return new ModelAndView("/wallet");
    }
    @GetMapping("/wallet/topup")
    public ModelAndView walletTopUpBalance() {
        return new ModelAndView("/topup");
    }

    @PostMapping("/wallet/topup")
    public ModelAndView walletTopUpPostBalance(Model model, Principal principal,
                               @RequestParam("operationType") String operationType,
                               @RequestParam(value = "currency", required = false) String currency,
                               @RequestParam(value = "balance", required = false) BigDecimal balance,
                               @RequestParam(value = "cryptoCurrency", required = false) String cryptoCurrency,
                               @RequestParam(value = "amount", required = false) BigDecimal amount) {
        String username = principal.getName();
        User user = userServiceImpl.getDetailsByUsername(username);

        if ("cryptoTopup".equals(operationType)) {
            try {
                if (amount != null) {
                    cryptoWalletServiceImpl.isNegativeCryptoWalletField(amount);
                    cryptoWalletServiceImpl.topUpCryptoBalance(username, cryptoCurrency, amount);
                    Long cryptoWalletId = cryptoWalletServiceImpl.getCryptoBalanceByUsernameAndCurrency(username, cryptoCurrency).getId();
                    transactionServiceImpl.performCryptoDepositTransaction(user.getId(), cryptoWalletId, cryptoCurrency, amount);
                    model.addAttribute("token", cryptoCurrency);
                    model.addAttribute("amount", amount);
                    model.addAttribute("SuccessCrypto", true);
                } else {
                    model.addAttribute("nullError", ErrorMessages.AT_LEAST_ONE_FIELD);
                }
            } catch (IllegalArgumentException e) {
                model.addAttribute("numberError", e.getMessage());
            }
        } else if ("moneyTopup".equals(operationType)) {
            try {
                if (balance != null) {
                    moneyWalletServiceImpl.isNegativeMoneyWalletField(balance);
                    moneyWalletServiceImpl.topUpMoneyBalance(username, currency, balance);
                    Long moneyWalletId = moneyWalletServiceImpl.getMoneyBalanceByUsernameAndCurrency(username, currency).getId();
                    transactionServiceImpl.performMoneyDepositTransaction(user.getId(), moneyWalletId, currency, balance);
                    model.addAttribute("currency", currency);
                    model.addAttribute("balance", balance);
                    model.addAttribute("SuccessMoney", true);
                } else {
                    model.addAttribute("nullError", ErrorMessages.AT_LEAST_ONE_FIELD);
                }
            } catch (IllegalArgumentException e) {
                model.addAttribute("numberError", e.getMessage());
            }
        }
        return new ModelAndView("/topup");
    }
    @GetMapping("/wallet/withdraw")
    public ModelAndView walletWithdrawFromBalance() {
        return new ModelAndView("/withdraw");
    }

    //TODO сделать уведомление на невозможность снятия больше чем есть в балансе
    @PostMapping("/wallet/withdraw")
    public ModelAndView walletWithDrawFromPostBalance(Model model, Principal principal,
                                               @RequestParam("operationType") String operationType,
                                               @RequestParam(value = "currency", required = false) String currency,
                                               @RequestParam(value = "balance", required = false) BigDecimal balance,
                                               @RequestParam(value = "cryptoCurrency", required = false) String cryptoCurrency,
                                               @RequestParam(value = "amount", required = false) BigDecimal amount) {
        String username = principal.getName();
        User user = userServiceImpl.getDetailsByUsername(username);

        if ("cryptoWithdraw".equals(operationType)) {
            try {
                if (amount != null) {
                    cryptoWalletServiceImpl.isNegativeCryptoWalletField(amount);
                    cryptoWalletServiceImpl.withdrawCryptoBalance(username, cryptoCurrency, amount);
                    Long cryptoWalletId = cryptoWalletServiceImpl.getCryptoBalanceByUsernameAndCurrency(username, cryptoCurrency).getId();
                    transactionServiceImpl.performCryptoWithdrawTransaction(user.getId(), cryptoWalletId, cryptoCurrency, amount);
                    model.addAttribute("token", cryptoCurrency);
                    model.addAttribute("amount", amount);
                    model.addAttribute("SuccessCrypto", true);
                } else {
                    model.addAttribute("nullError", ErrorMessages.AT_LEAST_ONE_FIELD);
                }
            } catch (IllegalArgumentException e) {
                model.addAttribute("numberError", e.getMessage());
            }
        } else if ("moneyWithdraw".equals(operationType)) {
            try {
                if (balance != null) {
                    moneyWalletServiceImpl.isNegativeMoneyWalletField(balance);
                    moneyWalletServiceImpl.withdrawMoneyBalance(username, currency, balance);
                    Long moneyWalletId = moneyWalletServiceImpl.getMoneyBalanceByUsernameAndCurrency(username, currency).getId();
                    transactionServiceImpl.performMoneyWithdrawTransaction(user.getId(), moneyWalletId, currency, balance);
                    model.addAttribute("currency", currency);
                    model.addAttribute("balance", balance);
                    model.addAttribute("SuccessMoney", true);
                } else {
                    model.addAttribute("nullError", ErrorMessages.AT_LEAST_ONE_FIELD);
                }
            } catch (IllegalArgumentException e) {
                model.addAttribute("numberError", e.getMessage());
            }
        }
        return new ModelAndView("/withdraw");
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
        String loggedUsername = principal.getName();
        User user = userServiceImpl.getDetailsByUsername(loggedUsername);
        Long userId = user.getId();

        try {
            userServiceImpl.isEmptySettingField(name, surname, phoneNumber, email, dateOfBirth);
            userServiceImpl.isExistedEmail(email);
            userServiceImpl.updateUserDetails(userId, name, surname, phoneNumber, email, dateOfBirth);
            model.addAttribute("Success", true);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals(ErrorMessages.AT_LEAST_ONE_FIELD)) {
                model.addAttribute("nullError", ErrorMessages.AT_LEAST_ONE_FIELD);
            } else if (e.getMessage().equals(ErrorMessages.EMAIL_TAKEN)) {
                model.addAttribute("emailError", ErrorMessages.EMAIL_TAKEN);
            }
        }
        User updatedUser = userServiceImpl.getDetailsByUsername(loggedUsername);
        model.addAttribute("user", updatedUser);

        return new ModelAndView("/setting");
    }
}
