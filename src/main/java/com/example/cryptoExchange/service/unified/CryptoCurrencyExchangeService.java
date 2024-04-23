package com.example.cryptoExchange.service.unified;

import com.example.cryptoExchange.Exceptions.GlobalExceptionHandler;
import com.example.cryptoExchange.constants.ErrorMessages;
import com.example.cryptoExchange.dto.CryptoCurrencyExchangeDTO;
import com.example.cryptoExchange.service.*;
import com.example.cryptoExchange.service.util.ValidationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;

import static com.example.cryptoExchange.constants.ViewAttribute.*;

@Service
public class CryptoCurrencyExchangeService {
    private final CryptoCurrencyService cryptoCurrencyService;
    private final MoneyWalletService moneyWalletService;
    private final GlobalExceptionHandler globalExceptionHandler;
    private final CryptoReserveBankService cryptoReserveBankService;
    private final CryptoWalletService cryptoWalletService;
    private final MoneyReserveBankService moneyReserveBankService;
    private final TransactionService transactionService;

    public CryptoCurrencyExchangeService(CryptoCurrencyService cryptoCurrencyService, MoneyWalletService moneyWalletService,
                                         GlobalExceptionHandler globalExceptionHandler, CryptoReserveBankService cryptoReserveBankService,
                                         CryptoWalletService cryptoWalletService, MoneyReserveBankService moneyReserveBankService,
                                         TransactionService transactionService) {
        this.cryptoCurrencyService = cryptoCurrencyService;
        this.moneyWalletService = moneyWalletService;
        this.globalExceptionHandler = globalExceptionHandler;
        this.cryptoReserveBankService = cryptoReserveBankService;
        this.cryptoWalletService = cryptoWalletService;
        this.moneyReserveBankService = moneyReserveBankService;
        this.transactionService = transactionService;
    }


    @Transactional
    public void processBuyCryptoTransaction(Model model, CryptoCurrencyExchangeDTO cryptoCurrencyExchangeDTO) {
        try {
            ValidationUtil.validateNumber(cryptoCurrencyExchangeDTO.getAmount());

            BigDecimal rate = BigDecimal.valueOf(1.05).multiply(cryptoCurrencyService.getCryptoCurrencyBySymbol(cryptoCurrencyExchangeDTO.getCryptoCurrency()).getRate());
            BigDecimal totalPrice = rate.multiply(cryptoCurrencyExchangeDTO.getAmount());
            BigDecimal sumBalance = moneyWalletService.getMoneyBalanceByUsernameAndCurrency(cryptoCurrencyExchangeDTO.getUsername(), cryptoCurrencyExchangeDTO.getCurrency()).getBalance();

            if (totalPrice.compareTo(sumBalance) <= 0) {
                handleBuyCryptoSuccess(model,
                        cryptoCurrencyExchangeDTO.getUsername(),
                        cryptoCurrencyExchangeDTO.getCurrency(),
                        cryptoCurrencyExchangeDTO.getCryptoCurrency(),
                        cryptoCurrencyExchangeDTO.getAmount(),
                        totalPrice, rate);
            } else {
                model.addAttribute(NOBALANCE_MARK, ErrorMessages.INSUFFICIENT_BALANCE);
            }
        } catch (IllegalArgumentException e) {
            globalExceptionHandler.handleExchangeException(model, e);
        }
    }
    @Transactional
    public void processSellCryptoTransaction(Model model, CryptoCurrencyExchangeDTO cryptoCurrencyExchangeDTO) {
        try {
            ValidationUtil.validateNumber(cryptoCurrencyExchangeDTO.getAmount());

            BigDecimal rate = BigDecimal.valueOf(0.95).multiply(cryptoCurrencyService.getCryptoCurrencyBySymbol(cryptoCurrencyExchangeDTO.getCryptoCurrency()).getRate());
            BigDecimal totalPrice = rate.multiply(cryptoCurrencyExchangeDTO.getAmount());
            BigDecimal sumCryptoBalance = cryptoWalletService.getCryptoBalanceByUsernameAndCurrency(cryptoCurrencyExchangeDTO.getUsername(), cryptoCurrencyExchangeDTO.getCryptoCurrency()).getAmount();

            if (cryptoCurrencyExchangeDTO.getAmount().compareTo(sumCryptoBalance) <= 0) {
                handleSellCryptoSuccess(model,
                        cryptoCurrencyExchangeDTO.getUsername(),
                        cryptoCurrencyExchangeDTO.getCurrency(),
                        cryptoCurrencyExchangeDTO.getCryptoCurrency(),
                        cryptoCurrencyExchangeDTO.getAmount(),
                        totalPrice, rate);
            } else if (cryptoCurrencyExchangeDTO.getAmount().compareTo(sumCryptoBalance) > 0) {
                model.addAttribute(NOBALANCE_MARK, ErrorMessages.INSUFFICIENT_BALANCE);
            }
        } catch (IllegalArgumentException e) {
            globalExceptionHandler.handleExchangeException(model, e);
        }

    }
    public void handleBuyCryptoSuccess(Model model, String username, String currency, String cryptoCurrency, BigDecimal amount, BigDecimal totalPrice, BigDecimal rate) {
        cryptoReserveBankService.checkCryptoReserveBankBalanceSufficiency(cryptoCurrency, amount);

        perfomBuyCryptoTransaction(username, currency, cryptoCurrency, amount, totalPrice, rate);

        model.addAttribute(BUY_SUCCESS, true)
                .addAttribute(TOTALPRICE_MARK, totalPrice);
    }
    public void handleSellCryptoSuccess(Model model, String username, String currency, String cryptoCurrency, BigDecimal amount, BigDecimal totalPrice, BigDecimal rate) {
        moneyReserveBankService.isEnoughMoneyReserveBankBalance(currency, totalPrice);

        perfomSellCryptoTransaction(username, currency, cryptoCurrency, amount, totalPrice, rate);

        model.addAttribute(SELL_SUCCESS, true)
                .addAttribute(TOTALPRICE_MARK, totalPrice);
    }
    public void perfomBuyCryptoTransaction(String username, String currency, String cryptoCurrency, BigDecimal amount, BigDecimal totalPrice, BigDecimal rate) {
        Long cryptoWalletId = cryptoWalletService.getCryptoBalanceByUsernameAndCurrency(username, cryptoCurrency).getId();

        BigDecimal newMoneyReserveBankBalance = moneyReserveBankService.getMoneyReserveBankBalanceById(currency).add(totalPrice);
        BigDecimal newUserMoneyWalletBalance = moneyWalletService.getMoneyBalanceByUsernameAndCurrency(username, currency).getBalance().subtract(totalPrice);
        BigDecimal newUserCryptoReserveBankBalance = cryptoReserveBankService.getCryptoReserveBankBalanceBySymbol(cryptoCurrency).subtract(amount);
        BigDecimal newUserCryptoWalletBalance = cryptoWalletService.getCryptoBalanceByUsernameAndCurrency(username, cryptoCurrency).getAmount().add(amount);
        moneyReserveBankService.updateMoneyReserveBankByCurrency(currency, newMoneyReserveBankBalance);
        moneyWalletService.updateMoneyWalletByCurrencyAndUser(username, currency, newUserMoneyWalletBalance);
        cryptoReserveBankService.updateCryptoReserveBankByCryptoCurrency(cryptoCurrency, newUserCryptoReserveBankBalance);
        cryptoWalletService.updateCryptoWalletByCryptoCurrencyAndUser(username, cryptoCurrency, newUserCryptoWalletBalance);

        transactionService.saveCryptoBuyTransaction(username, cryptoWalletId, cryptoCurrency, amount, totalPrice, rate);
    }
    public void perfomSellCryptoTransaction(String username, String currency, String cryptoCurrency, BigDecimal amount, BigDecimal totalPrice, BigDecimal rate) {
        Long cryptoWalletId = cryptoWalletService.getCryptoBalanceByUsernameAndCurrency(username, cryptoCurrency).getId();

        BigDecimal newUserCryptoReserveBankBalance = cryptoReserveBankService.getCryptoReserveBankBalanceBySymbol(cryptoCurrency).add(amount);
        BigDecimal newUserMoneyWalletBalance = moneyWalletService.getMoneyBalanceByUsernameAndCurrency(username, currency).getBalance().add(totalPrice);
        BigDecimal newUserCryptoWalletBalance = cryptoWalletService.getCryptoBalanceByUsernameAndCurrency(username, cryptoCurrency).getAmount().subtract(amount);
        BigDecimal newMoneyReserveBankBalance = moneyReserveBankService.getMoneyReserveBankBalanceById(currency).subtract(totalPrice);
        cryptoReserveBankService.updateCryptoReserveBankByCryptoCurrency(cryptoCurrency, newUserCryptoReserveBankBalance);
        moneyWalletService.updateMoneyWalletByCurrencyAndUser(username, currency, newUserMoneyWalletBalance);
        cryptoWalletService.updateCryptoWalletByCryptoCurrencyAndUser(username, cryptoCurrency, newUserCryptoWalletBalance);
        moneyReserveBankService.updateMoneyReserveBankByCurrency(currency, newMoneyReserveBankBalance);

        transactionService.saveCryptoSellTransaction(username, cryptoWalletId, cryptoCurrency, amount, totalPrice, rate);
    }
}
