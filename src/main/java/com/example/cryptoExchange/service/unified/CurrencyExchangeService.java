package com.example.cryptoExchange.service.unified;

import com.example.cryptoExchange.Exceptions.GlobalExceptionHandler;
import com.example.cryptoExchange.constants.ErrorMessages;
import com.example.cryptoExchange.dto.CurrencyExchangeDTO;
import com.example.cryptoExchange.service.*;
import com.example.cryptoExchange.service.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;

import static com.example.cryptoExchange.constants.ViewAttribute.*;

@Service
public class CurrencyExchangeService {
    private final MoneyReserveBankService moneyReserveBankService;
    private final MoneyWalletService moneyWalletService;
    private final TransactionService transactionService;
    private final CurrencyService currencyService;
    private final GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    public CurrencyExchangeService(MoneyReserveBankService moneyReserveBankService,
                                   MoneyWalletService moneyWalletService,
                                   TransactionService transactionService,
                                   CurrencyService currencyService,
                                   GlobalExceptionHandler globalExceptionHandler) {
        this.moneyReserveBankService = moneyReserveBankService;
        this.moneyWalletService = moneyWalletService;
        this.transactionService = transactionService;
        this.currencyService = currencyService;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @Transactional
    public void processBuyTransaction(Model model, CurrencyExchangeDTO currencyExchangeDTO) {
        try {
            ValidationUtil.validateNumber(currencyExchangeDTO.getAmount());
            BigDecimal rate = BigDecimal.valueOf(1.05).multiply(currencyService.getCurrencyBySymbol(currencyExchangeDTO.getCurrencyToBuy()).getRate());
            BigDecimal totalPriceRub = rate.multiply(currencyExchangeDTO.getAmount());
            BigDecimal sumBalance = moneyWalletService.getMoneyBalanceByUsernameAndCurrency(currencyExchangeDTO.getUsername(), currencyExchangeDTO.getCurrencyToSell()).getBalance();

            if (totalPriceRub.compareTo(sumBalance) <= 0) {
                handleBuySuccess(model, currencyExchangeDTO.getCurrencyToBuy(), currencyExchangeDTO.getCurrencyToSell(), currencyExchangeDTO.getUsername(),
                        currencyExchangeDTO.getAmount(), totalPriceRub, rate);
            } else {
                model.addAttribute(NOBALANCE_MARK, ErrorMessages.INSUFFICIENT_BALANCE);
            }
        } catch (IllegalArgumentException e) {
            globalExceptionHandler.handleExchangeException(model, e);
        }
    }


    @Transactional
    public void processSellTransaction(Model model, CurrencyExchangeDTO currencyExchangeDTO) {
        try {
            ValidationUtil.validateNumber(currencyExchangeDTO.getAmount());
            BigDecimal rate = BigDecimal.valueOf(0.95).multiply(currencyService.getCurrencyBySymbol(currencyExchangeDTO.getCurrencyToSell()).getRate());
            BigDecimal totalPriceRub = rate.multiply(currencyExchangeDTO.getAmount());
            BigDecimal sumBalance = moneyWalletService.getMoneyBalanceByUsernameAndCurrency(currencyExchangeDTO.getUsername(), currencyExchangeDTO.getCurrencyToSell()).getBalance();

            if (currencyExchangeDTO.getAmount().compareTo(sumBalance) <= 0) {
                handleSellSuccess(model, currencyExchangeDTO.getCurrencyToBuy(), currencyExchangeDTO.getCurrencyToSell(), currencyExchangeDTO.getUsername(),
                        currencyExchangeDTO.getAmount(), totalPriceRub, rate);
            } else {
                model.addAttribute(NOBALANCE_MARK, ErrorMessages.INSUFFICIENT_BALANCE);
            }
        } catch (IllegalArgumentException e) {
            globalExceptionHandler.handleExchangeException(model, e);
        }
    }
    public void handleBuySuccess(Model model,
                                 String currencyToBuy, String currencyToSell, String username,
                                 BigDecimal amount, BigDecimal totalPriceRub, BigDecimal rate) {
        moneyReserveBankService.isEnoughMoneyReserveBankBalance(currencyToBuy, amount);

        perfomBuyExchangeTransaction(currencyToBuy, currencyToSell, username,
                amount, totalPriceRub, rate);
        model.addAttribute(BUY_SUCCESS, true)
                .addAttribute(CURRENCY_MARK, currencyToSell)
                .addAttribute(TOTALPRICE_MARK, totalPriceRub);
    }

    public void handleSellSuccess(Model model, String currencyToBuy, String currencyToSell, String username,
                                  BigDecimal amount, BigDecimal totalPriceRub, BigDecimal rate) {
        moneyReserveBankService.isEnoughMoneyReserveBankBalance(currencyToBuy, totalPriceRub);

        perfomSellExchangeTransaction(currencyToBuy, currencyToSell, username,
                amount, totalPriceRub, rate);
        model.addAttribute(SELL_SUCCESS, true)
                .addAttribute(CURRENCY_MARK, currencyToBuy)
                .addAttribute(TOTALPRICE_MARK, totalPriceRub);
    }
    public void perfomBuyExchangeTransaction(String currencyToBuy, String currencyToSell, String username,
                                             BigDecimal amount, BigDecimal totalPriceRub, BigDecimal rate) {

        Long moneyWalletId = moneyWalletService.getMoneyBalanceByUsernameAndCurrency(username, currencyToBuy).getId();

        BigDecimal currentMoneyReserveBankBalanceToSell = moneyReserveBankService.getMoneyReserveBankBalanceById(currencyToSell);
        BigDecimal currentMoneyWalletBalanceToSell = moneyWalletService.getMoneyBalanceByUsernameAndCurrency(username, currencyToSell).getBalance();
        BigDecimal currentMoneyReserveBankBalanceToBuy = moneyReserveBankService.getMoneyReserveBankBalanceById(currencyToBuy);
        BigDecimal currentMoneyWalletBalanceToBuy = moneyWalletService.getMoneyBalanceByUsernameAndCurrency(username, currencyToBuy).getBalance();

        BigDecimal newMoneyReserveBankBalanceToSell = currentMoneyReserveBankBalanceToSell.add(totalPriceRub);
        BigDecimal newUserMoneyWalletBalanceToSell = currentMoneyWalletBalanceToSell.subtract(totalPriceRub);
        BigDecimal newMoneyReserveBankBalanceToBuy = currentMoneyReserveBankBalanceToBuy.subtract(amount);
        BigDecimal newUserMoneyWalletBalanceToBuy = currentMoneyWalletBalanceToBuy.add(amount);

        moneyReserveBankService.updateBalancesInTransaction(currencyToBuy, currencyToSell, newMoneyReserveBankBalanceToBuy,  newMoneyReserveBankBalanceToSell);
        moneyWalletService.updateBalancesInTransaction(username, currencyToBuy, currencyToSell, newUserMoneyWalletBalanceToBuy, newUserMoneyWalletBalanceToSell);

        transactionService.saveMoneyBuyTransaction(username, moneyWalletId, currencyToBuy, amount, totalPriceRub, rate);
    }

    public void perfomSellExchangeTransaction(String currencyToBuy, String currencyToSell, String username,
                                              BigDecimal amount, BigDecimal totalPriceRub, BigDecimal rate) {
        Long moneyWalletId = moneyWalletService.getMoneyBalanceByUsernameAndCurrency(username, currencyToBuy).getId();

        BigDecimal currentMoneyReserveBankBalanceToSell = moneyReserveBankService.getMoneyReserveBankBalanceById(currencyToSell);
        BigDecimal currentMoneyWalletBalanceToSell = moneyWalletService.getMoneyBalanceByUsernameAndCurrency(username, currencyToSell).getBalance();
        BigDecimal currentMoneyReserveBankBalanceToBuy = moneyReserveBankService.getMoneyReserveBankBalanceById(currencyToBuy);
        BigDecimal currentMoneyWalletBalanceToBuy = moneyWalletService.getMoneyBalanceByUsernameAndCurrency(username, currencyToBuy).getBalance();

        BigDecimal newMoneyReserveBankBalanceToSell = currentMoneyReserveBankBalanceToSell.add(amount);
        BigDecimal newUserMoneyWalletBalanceToSell = currentMoneyWalletBalanceToSell.subtract(amount);
        BigDecimal newMoneyReserveBankBalanceToBuy = currentMoneyReserveBankBalanceToBuy.subtract(totalPriceRub);
        BigDecimal newUserMoneyWalletBalanceToBuy = currentMoneyWalletBalanceToBuy.add(totalPriceRub);

        moneyReserveBankService.updateBalancesInTransaction(currencyToBuy, currencyToSell, newMoneyReserveBankBalanceToBuy,  newMoneyReserveBankBalanceToSell);
        moneyWalletService.updateBalancesInTransaction(username, currencyToBuy, currencyToSell, newUserMoneyWalletBalanceToBuy, newUserMoneyWalletBalanceToSell);

        transactionService.saveMoneySellTransaction(username, moneyWalletId, currencyToSell, amount, totalPriceRub, rate);
    }
}
