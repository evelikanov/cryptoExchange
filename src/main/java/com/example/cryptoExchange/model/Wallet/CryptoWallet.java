package com.example.cryptoExchange.model.Wallet;


import com.example.cryptoExchange.model.Transaction.Transaction;
import com.example.cryptoExchange.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class CryptoWallet extends Wallet {


    @Column
    private BigDecimal amount;
    @Column
    private String cryptoCurrency;
}
