package com.example.cryptoExchange.model.Wallet;

import com.example.cryptoExchange.model.Transaction.Transaction;
import com.example.cryptoExchange.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class MoneyWallet extends Wallet {

    @Column
    private BigDecimal balance;
    @Column
    private String currency;

}