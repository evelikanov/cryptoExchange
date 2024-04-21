package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.Exceptions.GlobalExceptionHandler;
import com.example.cryptoExchange.constants.ErrorMessages;
import com.example.cryptoExchange.dto.CurrencyExchangeDTO;
import com.example.cryptoExchange.model.Transaction.Transaction;
import com.example.cryptoExchange.model.Wallet.CryptoWallet;
import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.model.Wallet.MoneyWallet;
import com.example.cryptoExchange.repository.WalletRepository.CryptoWalletRepository;
import com.example.cryptoExchange.repository.TransactionRepository;
import com.example.cryptoExchange.repository.WalletRepository.MoneyWalletRepository;
import com.example.cryptoExchange.service.TransactionService;
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

import static com.example.cryptoExchange.constants.ErrorMessages.*;
import static com.example.cryptoExchange.constants.TransactionMessages.*;
import static com.example.cryptoExchange.constants.UrlAddress.*;
import static com.example.cryptoExchange.constants.UserDataMessages.*;
import static com.example.cryptoExchange.constants.ViewAttribute.*;

@RestController
@RequestMapping(_USER)
@Slf4j
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
    @Autowired
    private CryptoCurrencyServiceImpl cryptoCurrencyServiceImpl;
    @Autowired
    private CurrencyServiceImpl currencyServiceImpl;
    @Autowired
    private MoneyReserveBankServiceImpl moneyReserveBankServiceImpl;
    @Autowired
    private CryptoReserveBankServiceImpl cryptoReserveBankServiceImpl;
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
    public ModelAndView buySellGetService(Model model) {
        return new ModelAndView(_BUYSELLSERVICE);
    }
    @PostMapping(_BUYSELLSERVICE)
    public ModelAndView buySellPostService(Model model, Principal principal,
                                           @RequestParam(CRYPTO_CURRENCY) String cryptoCurrency,
                                           @RequestParam(CURRENCY) String currency,
                                           @RequestParam(TRANSACTION_TYPE) String transactionType,
                                           @RequestParam(value = AMOUNT, required = false) BigDecimal amount) {
        String username = principal.getName();
        Long cryptoWalletId = cryptoWalletServiceImpl.getCryptoBalanceByUsernameAndCurrency(username, cryptoCurrency).getId();

        if (transactionType.equals(TRANSACTION_TYPE_BUY)) {
            try {
                ValidationUtil.validateNumber(amount);
                BigDecimal rate = BigDecimal.valueOf(1.05).multiply(cryptoCurrencyServiceImpl.getCryptoCurrencyBySymbol(cryptoCurrency).getRate());
                BigDecimal totalPrice = rate.multiply(amount);
                BigDecimal sumBalance = moneyWalletServiceImpl.getMoneyBalanceByUsernameAndCurrency(username, currency).getBalance();
                if (totalPrice.compareTo(sumBalance) <= 0) {
                    ValidationUtil.validateNumber(amount);
                    cryptoReserveBankServiceImpl.isEnoughCryptoReserveBankBalance(cryptoCurrency, amount);
                    BigDecimal newMoneyReserveBankBalance = moneyReserveBankServiceImpl.getMoneyReserveBankBalanceById(currency).add(totalPrice);
                    BigDecimal newUserMoneyWalletBalance = moneyWalletServiceImpl.getMoneyBalanceByUsernameAndCurrency(username, currency).getBalance().subtract(totalPrice);
                    BigDecimal newUserCryptoReserveBankBalance = cryptoReserveBankServiceImpl.getCryptoReserveBankBalanceBySymbol(cryptoCurrency).subtract(amount);
                    BigDecimal newUserCryptoWalletBalance = cryptoWalletServiceImpl.getCryptoBalanceByUsernameAndCurrency(username, cryptoCurrency).getAmount().add(amount);
                    moneyReserveBankServiceImpl.updateMoneyReserveBankByCurrency(currency, newMoneyReserveBankBalance);
                    moneyWalletServiceImpl.updateMoneyWalletByCurrencyIdAndUser(username, currency, newUserMoneyWalletBalance);
                    cryptoReserveBankServiceImpl.updateCryptoReserveBankByCryptoCurrency(cryptoCurrency, newUserCryptoReserveBankBalance);
                    cryptoWalletServiceImpl.updateCryptoWalletByCryptoCurrencyIdAndUser(username, cryptoCurrency, newUserCryptoWalletBalance);

                    transactionServiceImpl.saveCryptoBuyTransaction(username, cryptoWalletId, cryptoCurrency, amount, totalPrice, rate);
                    model.addAttribute(BUY_SUCCESS, true)
                            .addAttribute(TOTALPRICE_MARK, totalPrice);
                } else if (totalPrice.compareTo(sumBalance) > 0) {
                    model.addAttribute(NOBALANCE_MARK, ErrorMessages.INSUFFICIENT_BALANCE);
                }
            } catch (IllegalArgumentException e) {
                globalExceptionHandler.handleExchangeException(model, e);
            }
        } else if (transactionType.equals(TRANSACTION_TYPE_SELL)) {
            try {
                ValidationUtil.validateNumber(amount);
                BigDecimal rate = BigDecimal.valueOf(0.95).multiply(cryptoCurrencyServiceImpl.getCryptoCurrencyBySymbol(cryptoCurrency).getRate());
                BigDecimal totalPrice = rate.multiply(amount);
                BigDecimal sumCryptoBalance = cryptoWalletServiceImpl.getCryptoBalanceByUsernameAndCurrency(username, cryptoCurrency).getAmount();
                if (amount.compareTo(sumCryptoBalance) <= 0) {
                    ValidationUtil.validateNumber(amount);
                    moneyReserveBankServiceImpl.isEnoughMoneyReserveBankBalance(currency, totalPrice);
                    BigDecimal newUserCryptoReserveBankBalance = cryptoReserveBankServiceImpl.getCryptoReserveBankBalanceBySymbol(cryptoCurrency).add(amount);
                    BigDecimal newUserMoneyWalletBalance = moneyWalletServiceImpl.getMoneyBalanceByUsernameAndCurrency(username, currency).getBalance().add(totalPrice);
                    BigDecimal newUserCryptoWalletBalance = cryptoWalletServiceImpl.getCryptoBalanceByUsernameAndCurrency(username, cryptoCurrency).getAmount().subtract(amount);
                    BigDecimal newMoneyReserveBankBalance = moneyReserveBankServiceImpl.getMoneyReserveBankBalanceById(currency).subtract(totalPrice);
                    cryptoReserveBankServiceImpl.updateCryptoReserveBankByCryptoCurrency(cryptoCurrency, newUserCryptoReserveBankBalance);
                    moneyWalletServiceImpl.updateMoneyWalletByCurrencyIdAndUser(username, currency, newUserMoneyWalletBalance);
                    cryptoWalletServiceImpl.updateCryptoWalletByCryptoCurrencyIdAndUser(username, cryptoCurrency, newUserCryptoWalletBalance);
                    moneyReserveBankServiceImpl.updateMoneyReserveBankByCurrency(currency, newMoneyReserveBankBalance);

                    transactionServiceImpl.saveCryptoSellTransaction(username, cryptoWalletId, cryptoCurrency, amount, totalPrice, rate);
                    model.addAttribute(SELL_SUCCESS, true)
                            .addAttribute(TOTALPRICE_MARK, totalPrice);
                    } else if (amount.compareTo(sumCryptoBalance) > 0) {
                        model.addAttribute(NOBALANCE_MARK, ErrorMessages.INSUFFICIENT_BALANCE);
                    }
            } catch (IllegalArgumentException e) {
                globalExceptionHandler.handleExchangeException(model, e);
            }
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
                               @RequestParam(OPERATION_TYPE) String operationType,
                               @RequestParam(value = CURRENCY, required = false) String currency,
                               @RequestParam(value = BALANCE, required = false) BigDecimal balance,
                               @RequestParam(value = CRYPTO_CURRENCY, required = false) String cryptoCurrency,
                               @RequestParam(value = AMOUNT, required = false) BigDecimal amount) {
        String username = principal.getName();
        if (CRYPTOCURRENCY_TOPUP.equals(operationType)) {
            try {
                ValidationUtil.validateNumber(amount);
                cryptoWalletServiceImpl.topUpCryptoBalance(username, cryptoCurrency, amount);
                Long cryptoWalletId = cryptoWalletServiceImpl.getCryptoBalanceByUsernameAndCurrency(username, cryptoCurrency).getId();
                transactionServiceImpl.saveCryptoDepositTransaction(username, cryptoWalletId, cryptoCurrency, amount);
                model.addAttribute(CRYPTOCURRENCY_MARK, cryptoCurrency)
                        .addAttribute(AMOUNT_MARK, amount)
                        .addAttribute(TOPUP_CRYPTO_SUCCESS, true);
            } catch (IllegalArgumentException e) {
                model.addAttribute(WRONGNUMBER_MARK, e.getMessage());
            }
        } else if (CURRENCY_TOPUP.equals(operationType)) {
            try {
                ValidationUtil.validateNumber(amount);
                moneyWalletServiceImpl.topUpMoneyBalance(username, currency, balance);
                Long moneyWalletId = moneyWalletServiceImpl.getMoneyBalanceByUsernameAndCurrency(username, currency).getId();
                transactionServiceImpl.saveMoneyDepositTransaction(username, moneyWalletId, currency, balance);
                model.addAttribute(CURRENCY_MARK, currency)
                        .addAttribute(BALANCE_MARK, balance)
                        .addAttribute(TOPUP_CURRENCY_SUCCESS, true);
            } catch (IllegalArgumentException e) {
                model.addAttribute(WRONGNUMBER_MARK , e.getMessage());
            }
        }
        return new ModelAndView(_TOPUP);
    }
    @GetMapping(_WALLET_WITHDRAW)
    public ModelAndView walletWithdrawFromBalance() {
        return new ModelAndView(_WITHDRAW);
    }

    @PostMapping(_WALLET_WITHDRAW)
    public ModelAndView walletWithDrawFromPostBalance(Model model, Principal principal,
                                               @RequestParam(OPERATION_TYPE) String operationType,
                                               @RequestParam(value = CURRENCY, required = false) String currency,
                                               @RequestParam(value = BALANCE, required = false) BigDecimal balance,
                                               @RequestParam(value = CRYPTO_CURRENCY, required = false) String cryptoCurrency,
                                               @RequestParam(value = AMOUNT, required = false) BigDecimal amount) {
        String username = principal.getName();

        if (CRYPTOCURRENCY_WITHDRAW.equals(operationType)) {
            try {
                ValidationUtil.validateNumber(amount);
                cryptoWalletServiceImpl.isEnoughCryptoBalance(username, cryptoCurrency, amount);
                cryptoWalletServiceImpl.withdrawCryptoBalance(username, cryptoCurrency, amount);
                Long cryptoWalletId = cryptoWalletServiceImpl.getCryptoBalanceByUsernameAndCurrency(username, cryptoCurrency).getId();
                transactionServiceImpl.saveCryptoWithdrawTransaction(username, cryptoWalletId, cryptoCurrency, amount);
                model.addAttribute(CRYPTOCURRENCY_MARK, cryptoCurrency)
                        .addAttribute(AMOUNT_MARK, amount)
                        .addAttribute(WITHDRAW_CRYPTO_SUCCESS, true);
            } catch (IllegalArgumentException e) {
                if (e.getMessage().equals(NEGATIVE_NUMBER)) {
                    model.addAttribute(WRONGNUMBER_MARK, e.getMessage());
                } else if (e.getMessage().equals(INSUFFICIENT_BALANCE)) {
                    model.addAttribute(NOBALANCE_MARK, INSUFFICIENT_BALANCE);
                }
                return new ModelAndView(_WITHDRAW);
            }
        } else if (CURRENCY_WITHDRAW.equals(operationType)) {
            try {
                ValidationUtil.validateNumber(balance);
                moneyWalletServiceImpl.isEnoughMoneyBalance(username, currency, balance);
                moneyWalletServiceImpl.withdrawMoneyBalance(username, currency, balance);
                Long moneyWalletId = moneyWalletServiceImpl.getMoneyBalanceByUsernameAndCurrency(username, currency).getId();
                transactionServiceImpl.saveMoneyWithdrawTransaction(username, moneyWalletId, currency, balance);
                model.addAttribute(CURRENCY_MARK, currency)
                        .addAttribute(BALANCE_MARK, balance)
                        .addAttribute(WITHDRAW_CURRENCY_SUCCESS, true);
            } catch (IllegalArgumentException e) {
                if (e.getMessage().equals(NEGATIVE_NUMBER)) {
                    model.addAttribute(WRONGNUMBER_MARK, e.getMessage());
                } else if (e.getMessage().equals(INSUFFICIENT_BALANCE)) {
                    model.addAttribute(NOBALANCE_MARK, INSUFFICIENT_BALANCE);
                }
                return new ModelAndView(_WITHDRAW);
            }
        }
        return new ModelAndView(_WITHDRAW);
    }
    @GetMapping(_SETTING)
    public ModelAndView setting(Model model, Principal principal) {
        String username = principal.getName();
        User user = userServiceImpl.getDetailsByUsername(username);
        model.addAttribute(LOGGED_USER, user);
        return new ModelAndView(_SETTING);
    }
    @PutMapping(_SETTING)
    public ModelAndView settingChange(Model model, Principal principal,
                                      @RequestParam(NAME) String name,
                                      @RequestParam(SURNAME) String surname,
                                      @RequestParam(PHONE_NUMBER) String phoneNumber,
                                      @RequestParam(EMAIL) String email,
                                      @RequestParam(DATE_OF_BIRTH) String dateOfBirth) {
        String loggedUsername = principal.getName();
        User user = userServiceImpl.getDetailsByUsername(loggedUsername);
        Long userId = user.getId();

        try {
            userServiceImpl.isEmptySettingField(name, surname, phoneNumber, email, dateOfBirth);
            userServiceImpl.isExistedEmail(email);
            userServiceImpl.updateUserDetails(userId, name, surname, phoneNumber, email, dateOfBirth);
            model.addAttribute(SUCCESS, true);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals(ErrorMessages.AT_LEAST_ONE_FIELD)) {
                model.addAttribute(NULL_MARK, ErrorMessages.AT_LEAST_ONE_FIELD);
            } else if (e.getMessage().equals(ErrorMessages.EMAIL_TAKEN)) {
                model.addAttribute(EMAIL_MARK, ErrorMessages.EMAIL_TAKEN);
            }
        }
        User updatedUser = userServiceImpl.getDetailsByUsername(loggedUsername);
        model.addAttribute(LOGGED_USER, updatedUser);

        return new ModelAndView(_SETTING);
    }
}
