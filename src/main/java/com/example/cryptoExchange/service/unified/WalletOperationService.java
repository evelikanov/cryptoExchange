package com.example.cryptoExchange.service.unified;

import com.example.cryptoExchange.Exceptions.GlobalExceptionHandler;
import com.example.cryptoExchange.constants.ErrorMessages;
import com.example.cryptoExchange.dto.TopupWalletDTO;
import com.example.cryptoExchange.dto.WIthdrawalDTO;
import com.example.cryptoExchange.model.Wallet.CryptoWallet;
import com.example.cryptoExchange.model.Wallet.MoneyWallet;
import com.example.cryptoExchange.service.CryptoWalletService;
import com.example.cryptoExchange.service.MoneyWalletService;
import com.example.cryptoExchange.service.TransactionService;
import com.example.cryptoExchange.service.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;

import static com.example.cryptoExchange.constants.ErrorMessages.INSUFFICIENT_BALANCE;
import static com.example.cryptoExchange.constants.ErrorMessages.NEGATIVE_NUMBER;
import static com.example.cryptoExchange.constants.ViewAttribute.*;

@Slf4j
@Service
public class WalletOperationService {
    private final MoneyWalletService moneyWalletService;
    private final CryptoWalletService cryptoWalletService;
    private final TransactionService transactionService;
    private final GlobalExceptionHandler globalExceptionHandler;

    public WalletOperationService(MoneyWalletService moneyWalletService, CryptoWalletService cryptoWalletService,
                                  TransactionService transactionService, GlobalExceptionHandler globalExceptionHandler) {
        this.moneyWalletService = moneyWalletService;
        this.cryptoWalletService = cryptoWalletService;
        this.transactionService = transactionService;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    public void getUserWalletData(Model model, String username) {
        List<MoneyWallet> wallet = moneyWalletService.getMoneyBalanceByUsername(username);
        List<CryptoWallet> cryptoWallets = cryptoWalletService.getCryptoBalanceByUsername(username);
        model.addAttribute(MONEYWALLET_MARK, wallet)
                .addAttribute(CRYPTOWALLET_MARK, cryptoWallets);
    }
    @Transactional
    public void replenishUserMoneyWallet(Model model, TopupWalletDTO topupWalletDTO) {
        try {
            ValidationUtil.validateNumber(topupWalletDTO.getBalance());

            moneyWalletService.topupMoneyBalance(topupWalletDTO.getUsername(), topupWalletDTO.getCurrency(), topupWalletDTO.getBalance());

            Long moneyWalletId = moneyWalletService.getMoneyBalanceByUsernameAndCurrency(topupWalletDTO.getUsername(), topupWalletDTO.getCurrency()).getId();
            transactionService.saveMoneyDepositTransaction(topupWalletDTO.getUsername(), moneyWalletId, topupWalletDTO.getCurrency(), topupWalletDTO.getBalance());

            model.addAttribute(CURRENCY_MARK, topupWalletDTO.getCurrency())
                    .addAttribute(BALANCE_MARK, topupWalletDTO.getBalance())
                    .addAttribute(TOPUP_CURRENCY_SUCCESS, true);

            log.error("User {} replenished his balance in the amount of {} {}", topupWalletDTO.getUsername(),
                    topupWalletDTO.getBalance(), topupWalletDTO.getCurrency());
        } catch (IllegalArgumentException e) {
            model.addAttribute(WRONGNUMBER_MARK, e.getMessage());

            log.error("User {} failed to replenish his balance: {}", topupWalletDTO.getUsername(), topupWalletDTO.getBalance());
        }
    }

    @Transactional
    public void replenishUserCryptoWallet(Model model, TopupWalletDTO topupWalletDTO) {
        try {
            ValidationUtil.validateNumber(topupWalletDTO.getAmount());

            cryptoWalletService.topupCryptoBalance(topupWalletDTO.getUsername(), topupWalletDTO.getCryptoCurrency(), topupWalletDTO.getAmount());

            Long cryptoWalletId = cryptoWalletService.getCryptoBalanceByUsernameAndCurrency(topupWalletDTO.getUsername(), topupWalletDTO.getCryptoCurrency()).getId();
            transactionService.saveCryptoDepositTransaction(topupWalletDTO.getUsername(), cryptoWalletId, topupWalletDTO.getCryptoCurrency(), topupWalletDTO.getAmount());

            model.addAttribute(CRYPTOCURRENCY_MARK, topupWalletDTO.getCryptoCurrency())
                    .addAttribute(AMOUNT_MARK, topupWalletDTO.getAmount())
                    .addAttribute(TOPUP_CRYPTO_SUCCESS, true);

            log.error("User {} replenished his balance in the amount of {} {}", topupWalletDTO.getUsername(),
                    topupWalletDTO.getAmount(), topupWalletDTO.getCryptoCurrency());
        } catch (IllegalArgumentException e) {
            model.addAttribute(WRONGNUMBER_MARK, e.getMessage());

            log.error("User {} failed to replenish his balance: {}", topupWalletDTO.getUsername(), topupWalletDTO.getAmount());
        }
    }

    @Transactional
    public void withdrawUserMoneyWallet(Model model, WIthdrawalDTO withdrawalDTO) {
        try {
            ValidationUtil.validateNumber(withdrawalDTO.getBalance());

            moneyWalletService.checkMoneyBalanceSufficiency(withdrawalDTO.getUsername(), withdrawalDTO.getCurrency(), withdrawalDTO.getBalance());
            moneyWalletService.withdrawMoneyBalance(withdrawalDTO.getUsername(), withdrawalDTO.getCurrency(), withdrawalDTO.getBalance());

            Long moneyWalletId = moneyWalletService.getMoneyBalanceByUsernameAndCurrency(withdrawalDTO.getUsername(), withdrawalDTO.getCurrency()).getId();
            transactionService.saveMoneyWithdrawTransaction(withdrawalDTO.getUsername(), moneyWalletId, withdrawalDTO.getCurrency(), withdrawalDTO.getBalance());

            model.addAttribute(CURRENCY_MARK, withdrawalDTO.getCurrency())
                    .addAttribute(BALANCE_MARK, withdrawalDTO.getBalance())
                    .addAttribute(WITHDRAW_CURRENCY_SUCCESS, true);

            log.info("User {} withdrew {} {}", withdrawalDTO.getUsername(), withdrawalDTO.getBalance(), withdrawalDTO.getCurrency());
        } catch (IllegalArgumentException e) {
            globalExceptionHandler.handleWithdrawalException(model, e);

            log.error("User {} failed to withdraw {} {}", withdrawalDTO.getUsername(), withdrawalDTO.getBalance(), withdrawalDTO.getCurrency());
        }
    }
    @Transactional
    public void withdrawUserCryptoWallet(Model model, WIthdrawalDTO withdrawalDTO) {
        try {
            ValidationUtil.validateNumber(withdrawalDTO.getAmount());

            cryptoWalletService.checkCryptoBalanceSufficiency(withdrawalDTO.getUsername(), withdrawalDTO.getCryptoCurrency(), withdrawalDTO.getAmount());
            cryptoWalletService.withdrawCryptoBalance(withdrawalDTO.getUsername(), withdrawalDTO.getCryptoCurrency(), withdrawalDTO.getAmount());

            Long cryptoWalletId = cryptoWalletService.getCryptoBalanceByUsernameAndCurrency(withdrawalDTO.getUsername(), withdrawalDTO.getCryptoCurrency()).getId();
            transactionService.saveCryptoWithdrawTransaction(withdrawalDTO.getUsername(), cryptoWalletId, withdrawalDTO.getCryptoCurrency(), withdrawalDTO.getAmount());

            model.addAttribute(CRYPTOCURRENCY_MARK, withdrawalDTO.getCryptoCurrency())
                    .addAttribute(AMOUNT_MARK, withdrawalDTO.getAmount())
                    .addAttribute(WITHDRAW_CRYPTO_SUCCESS, true);

            log.info("User {} withdrew {} {}", withdrawalDTO.getUsername(), withdrawalDTO.getAmount(), withdrawalDTO.getCryptoCurrency());
        } catch (IllegalArgumentException e) {
            globalExceptionHandler.handleWithdrawalException(model, e);

            log.error("User {} failed to withdraw {} {}", withdrawalDTO.getUsername(), withdrawalDTO.getAmount(), withdrawalDTO.getCryptoCurrency());
        }
    }



}
