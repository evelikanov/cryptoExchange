package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.Exceptions.GlobalExceptionHandler;
import com.example.cryptoExchange.constants.ErrorMessages;
import com.example.cryptoExchange.dto.*;
import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.repository.WalletRepository.CryptoWalletRepository;
import com.example.cryptoExchange.repository.TransactionRepository;
import com.example.cryptoExchange.repository.WalletRepository.MoneyWalletRepository;
import com.example.cryptoExchange.service.impl.CryptoReserveBankServiceImpl;
import com.example.cryptoExchange.service.impl.ExchangeCurrencyServiceImpl.CryptoCurrencyServiceImpl;
import com.example.cryptoExchange.service.impl.ExchangeCurrencyServiceImpl.CurrencyServiceImpl;
import com.example.cryptoExchange.service.impl.MoneyReserveBankServiceImpl;
import com.example.cryptoExchange.service.impl.WalletServiceImpl.CryptoWalletServiceImpl;
import com.example.cryptoExchange.service.impl.TransactionServiceImpl;
import com.example.cryptoExchange.service.impl.UserServiceImpl;
import com.example.cryptoExchange.service.impl.WalletServiceImpl.MoneyWalletServiceImpl;
import com.example.cryptoExchange.service.unified.*;
import com.example.cryptoExchange.service.util.ValidationUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.example.cryptoExchange.constants.TransactionMessages.*;
import static com.example.cryptoExchange.constants.UrlAddress.*;
import static com.example.cryptoExchange.constants.ViewAttribute.*;

@RestController
@RequestMapping(_USER)
@Slf4j
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private UpdatePriceService updatePriceService;
    @Autowired
    private CurrencyExchangeService currencyExchangeService;
    @Autowired
    private HistoryTransactionService historyTransactionService;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private WalletOperationService walletOperationService;
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;
    @Autowired
    private CryptoCurrencyExchangeService cryptoCurrencyExchangeService;

    @GetMapping(_CRYPTOCURRENCYLIST)
    public CompletableFuture<ModelAndView> cryptoCurrencyList(Model model) {
        return CompletableFuture.runAsync(() -> {
            try {
                updatePriceService.updateRates(model).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }).thenApply((result) -> {
            return new ModelAndView(_CRYPTOCURRENCYLIST);
        });
    }
    @GetMapping(_CURRENCYEXCHANGESERVICE)
    public ModelAndView currencyExchangeService() {
        return new ModelAndView(_CURRENCYEXCHANGESERVICE);
    }
    @PostMapping(_CURRENCYEXCHANGESERVICE)
    public ModelAndView currencyExchangePostService(Model model, Principal principal,
                                           CurrencyExchangeDTO currencyExchangeDTO) {
        currencyExchangeDTO.setUsername(principal.getName());

        if (currencyExchangeDTO.getOperationType().equals(TRANSACTION_TYPE_BUY)) {
            currencyExchangeService.processBuyTransaction(model, currencyExchangeDTO);
        } else if (currencyExchangeDTO.getOperationType().equals(TRANSACTION_TYPE_SELL)) {
            currencyExchangeService.processSellTransaction(model, currencyExchangeDTO);
        }
        return new ModelAndView(_CURRENCYEXCHANGESERVICE);
    }

    @GetMapping(_BUYSELLSERVICE)
    public ModelAndView buySellGetService() {
        return new ModelAndView(_BUYSELLSERVICE);
    }
    @PostMapping(_BUYSELLSERVICE)
    public ModelAndView cryptoExchangePostService(Model model, Principal principal,
                                                  CryptoCurrencyExchangeDTO cryptoCurrencyExchangeDTO) {
        String loggedUsername = principal.getName();
        cryptoCurrencyExchangeDTO.setUsername(loggedUsername);

        if (cryptoCurrencyExchangeDTO.getOperationType().equals(TRANSACTION_TYPE_BUY)) {
            cryptoCurrencyExchangeService.processBuyCryptoTransaction(model, cryptoCurrencyExchangeDTO);
        } else if (cryptoCurrencyExchangeDTO.getOperationType().equals(TRANSACTION_TYPE_SELL)) {
            cryptoCurrencyExchangeService.processSellCryptoTransaction(model, cryptoCurrencyExchangeDTO);
        }
        return new ModelAndView(_BUYSELLSERVICE);
    }

    @GetMapping(_DEALS)
    public ModelAndView deals(Model model, Principal principal) {
        historyTransactionService.getHistoryDeals(model, principal.getName());
        return new ModelAndView(_DEALS);
    }
    @GetMapping(_DATA)
    public ModelAndView data(Model model, Principal principal) {
        userDataService.getUserData(model, principal.getName());
        return new ModelAndView(_DATA);
    }

    @DeleteMapping(_DATA)
    public ModelAndView deleteAccount(HttpSession session, Principal principal) {
        userDataService.deleteUserAccount(session, principal.getName());
        RedirectView redirectView = new RedirectView(_HOME);
        redirectView.addStaticAttribute(DELETE_SUCCESS, true);
        return new ModelAndView(redirectView);
    }
    @GetMapping(_WALLET)
    public ModelAndView walletGetBalance(Model model, Principal principal) {
        walletOperationService.getUserWalletData(model, principal.getName());
        return new ModelAndView(_WALLET);
    }
    @GetMapping(_WALLET_TOPUP)
    public ModelAndView walletTopUpBalance() {
        return new ModelAndView(_TOPUP);
    }

    @PostMapping(_WALLET_TOPUP)
    public ModelAndView walletTopUpPostBalance(Model model, Principal principal,
                                               TopupWalletDTO topupWalletDTO) {
        String loggedUsername = principal.getName();
        topupWalletDTO.setUsername(loggedUsername);

        if (topupWalletDTO.getOperationType().equals(CURRENCY_TOPUP)) {
            walletOperationService.replenishUserMoneyWallet(model, topupWalletDTO);
        } else if (topupWalletDTO.getOperationType().equals(CRYPTOCURRENCY_TOPUP)) {
            walletOperationService.replenishUserCryptoWallet(model, topupWalletDTO);
        }
        return new ModelAndView(_TOPUP);
    }
    @GetMapping(_WALLET_WITHDRAW)
    public ModelAndView walletWithdrawFromBalance() {
        return new ModelAndView(_WITHDRAW);
    }

    @PostMapping(_WALLET_WITHDRAW)
    public ModelAndView walletWithDrawFromPostBalance(Model model, Principal principal,
                                                      WIthdrawalDTO withdrawalDTO) {
        String loggedUsername = principal.getName();
        withdrawalDTO.setUsername(loggedUsername);

        if (withdrawalDTO.getOperationType().equals(CURRENCY_WITHDRAW)) {
            walletOperationService.withdrawUserMoneyWallet(model, withdrawalDTO);
        } else if (withdrawalDTO.getOperationType().equals(CRYPTOCURRENCY_WITHDRAW)) {
            walletOperationService.withdrawUserCryptoWallet(model, withdrawalDTO);
        }
        return new ModelAndView(_WITHDRAW);
    }
    @GetMapping(_SETTING)
    public ModelAndView setting(Model model, Principal principal) {
        String loggedUsername = principal.getName();

        User user = userServiceImpl.getDetailsByUsername(loggedUsername);

        model.addAttribute(LOGGED_USER, user);
        return new ModelAndView(_SETTING);
    }
    @PutMapping(_SETTING)
    public ModelAndView settingChange(Model model, Principal principal,
                                      UserDataDTO userDataDTO) {
        String loggedUsername = principal.getName();
        userDataDTO.setUsername(loggedUsername);

        userDataService.updateUserDetails(model, userDataDTO);

        User updatedUser = userServiceImpl.getDetailsByUsername(userDataDTO.getUsername());
        model.addAttribute(LOGGED_USER, updatedUser);
        return new ModelAndView(_SETTING);
    }
}
