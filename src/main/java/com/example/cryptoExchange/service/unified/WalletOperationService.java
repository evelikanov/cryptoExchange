package com.example.cryptoExchange.service.unified;

import com.example.cryptoExchange.constants.ErrorMessages;
import com.example.cryptoExchange.model.Wallet.CryptoWallet;
import com.example.cryptoExchange.model.Wallet.MoneyWallet;
import com.example.cryptoExchange.service.CryptoWalletService;
import com.example.cryptoExchange.service.MoneyWalletService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

import static com.example.cryptoExchange.constants.ViewAttribute.*;

@Service
public class WalletOperationService {
    private final MoneyWalletService moneyWalletService;
    private final CryptoWalletService cryptoWalletService;

    public WalletOperationService(MoneyWalletService moneyWalletService, CryptoWalletService cryptoWalletService) {
        this.moneyWalletService = moneyWalletService;
        this.cryptoWalletService = cryptoWalletService;
    }

    public void getUserWalletData(Model model, String username) {
        List<MoneyWallet> wallet = moneyWalletService.getMoneyBalanceByUsername(username);
        List<CryptoWallet> cryptoWallets = cryptoWalletService.getCryptoBalanceByUsername(username);
        model.addAttribute(MONEYWALLET_MARK, wallet)
                .addAttribute(CRYPTOWALLET_MARK, cryptoWallets);
    }

    public void replenishUserMoneyWallet() {
//        try {
//            if (amount != null) {
//                cryptoWalletService.isNegativeCryptoWalletField(amount);
//                cryptoWalletServiceImpl.topUpCryptoBalance(username, cryptoCurrency, amount);
//                Long cryptoWalletId = cryptoWalletServiceImpl.getCryptoBalanceByUsernameAndCurrency(username, cryptoCurrency).getId();
//                transactionServiceImpl.saveCryptoDepositTransaction(username, cryptoWalletId, cryptoCurrency, amount);
//                model.addAttribute(CRYPTOCURRENCY_MARK, cryptoCurrency)
//                        .addAttribute(AMOUNT_MARK, amount)
//                        .addAttribute(TOPUP_CRYPTO_SUCCESS, true);
//            } else {
//                model.addAttribute(NULL_MARK, ErrorMessages.AT_LEAST_ONE_FIELD);
//            }
//        } catch (IllegalArgumentException e) {
//            model.addAttribute(WRONGNUMBER_MARK, e.getMessage());
//        }
    }
    public void replenishUserCryptoWallet() {

    }

}
