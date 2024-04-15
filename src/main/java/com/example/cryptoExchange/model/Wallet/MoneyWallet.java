package com.example.cryptoExchange.model.Wallet;

import com.example.cryptoExchange.model.ExchangeCurrency.Currency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Getter
@Setter
public class MoneyWallet extends Wallet {

    @Column
    private BigDecimal balance;
    @Column
    private String symbol;
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
}