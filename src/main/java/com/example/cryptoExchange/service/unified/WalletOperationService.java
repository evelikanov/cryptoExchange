package com.example.cryptoExchange.service.unified;

import com.example.cryptoExchange.constants.ErrorMessages;
import com.example.cryptoExchange.dto.TopupWalletDTO;
import com.example.cryptoExchange.model.Wallet.CryptoWallet;
import com.example.cryptoExchange.model.Wallet.MoneyWallet;
import com.example.cryptoExchange.service.CryptoWalletService;
import com.example.cryptoExchange.service.MoneyWalletService;
import com.example.cryptoExchange.service.TransactionService;
import com.example.cryptoExchange.service.util.ValidationUtil;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;

import static com.example.cryptoExchange.constants.ViewAttribute.*;

@Service
public class WalletOperationService {
    private final MoneyWalletService moneyWalletService;
    private final CryptoWalletService cryptoWalletService;
    private final TransactionService transactionService;

    public WalletOperationService(MoneyWalletService moneyWalletService, CryptoWalletService cryptoWalletService,
                                  TransactionService transactionService) {
        this.moneyWalletService = moneyWalletService;
        this.cryptoWalletService = cryptoWalletService;
        this.transactionService = transactionService;
    }

    public void getUserWalletData(Model model, String username) {
        List<MoneyWallet> wallet = moneyWalletService.getMoneyBalanceByUsername(username);
        List<CryptoWallet> cryptoWallets = cryptoWalletService.getCryptoBalanceByUsername(username);
        model.addAttribute(MONEYWALLET_MARK, wallet)
                .addAttribute(CRYPTOWALLET_MARK, cryptoWallets);
    }
    public void replenishUserMoneyWallet(Model model, TopupWalletDTO topupWalletDTO) {
        try {
            ValidationUtil.validateNumber(topupWalletDTO.getBalance());

            moneyWalletService.topupMoneyBalance(topupWalletDTO.getUsername(), topupWalletDTO.getCurrency(), topupWalletDTO.getBalance());

            Long moneyWalletId = moneyWalletService.getMoneyBalanceByUsernameAndCurrency(topupWalletDTO.getUsername(), topupWalletDTO.getCurrency()).getId();
            transactionService.saveMoneyDepositTransaction(topupWalletDTO.getUsername(), moneyWalletId, topupWalletDTO.getCurrency(), topupWalletDTO.getBalance());

            model.addAttribute(CURRENCY_MARK, topupWalletDTO.getCurrency())
                    .addAttribute(BALANCE_MARK, topupWalletDTO.getBalance())
                    .addAttribute(TOPUP_CURRENCY_SUCCESS, true);
        } catch (IllegalArgumentException e) {
            model.addAttribute(WRONGNUMBER_MARK, e.getMessage());
        }
    }

    public void replenishUserCryptoWallet(Model model, TopupWalletDTO topupWalletDTO) {
        try {
            ValidationUtil.validateNumber(topupWalletDTO.getAmount());

            cryptoWalletService.topupCryptoBalance(topupWalletDTO.getUsername(), topupWalletDTO.getCryptoCurrency(), topupWalletDTO.getAmount());

            Long cryptoWalletId = cryptoWalletService.getCryptoBalanceByUsernameAndCurrency(topupWalletDTO.getUsername(), topupWalletDTO.getCryptoCurrency()).getId();
            transactionService.saveCryptoDepositTransaction(topupWalletDTO.getUsername(), cryptoWalletId, topupWalletDTO.getCryptoCurrency(), topupWalletDTO.getAmount());

            model.addAttribute(CRYPTOCURRENCY_MARK, topupWalletDTO.getCryptoCurrency())
                    .addAttribute(AMOUNT_MARK, topupWalletDTO.getAmount())
                    .addAttribute(TOPUP_CRYPTO_SUCCESS, true);
        } catch (IllegalArgumentException e) {
            model.addAttribute(WRONGNUMBER_MARK, e.getMessage());
        }
    }



}
