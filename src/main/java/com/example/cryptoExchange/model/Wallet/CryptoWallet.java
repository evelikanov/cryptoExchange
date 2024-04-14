package com.example.cryptoExchange.model.Wallet;


import com.example.cryptoExchange.model.ExchangeCurrency.CryptoCurrency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Getter
@Setter
public class CryptoWallet extends Wallet {


    @Column
    private BigDecimal amount;
    @Column
    private String symbol;
    @ManyToOne
    @JoinColumn(name = "crypto_currency_id")
    private CryptoCurrency cryptoCurrency;

}
