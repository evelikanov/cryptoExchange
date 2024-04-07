package com.example.cryptoExchange.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


//TODO use this entity for user wallet
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true)
    private Long id;
    @ManyToOne
    private User user;
//    @ManyToOne
//    private CryptoCurrency currency;

    private BigDecimal currency;
    private BigDecimal balance;

}