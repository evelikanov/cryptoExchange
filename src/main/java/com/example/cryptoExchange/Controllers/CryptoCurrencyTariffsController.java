package com.example.cryptoExchange.Controllers;

import com.example.cryptoExchange.model.CryptoCurrencyTariffs;
import com.example.cryptoExchange.service.impl.ExchangeCurrencyServiceImpl.CryptoCurrencyServiceImpl;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
@NoArgsConstructor
public class CryptoCurrencyTariffsController {
    @Autowired
    private CryptoCurrencyServiceImpl cryptoCurrencyServiceImpl;

    @GetMapping("/tariffs")
    public ModelAndView cryptoCurrencyTariffs(Model model) {
        CryptoCurrencyTariffs[] cryptoSell = cryptoCurrencyServiceImpl.exchangeRateSell();
        CryptoCurrencyTariffs[] cryptoBuy = cryptoCurrencyServiceImpl.exchangeRateBuy();
        model.addAttribute("cryptoBuy", cryptoBuy);
        model.addAttribute("cryptoSell", cryptoSell);
        return new ModelAndView("/tariffs");
    }
}
