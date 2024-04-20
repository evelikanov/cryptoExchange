package com.example.cryptoExchange.model.ExchangeCurrency;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Table(name = "currency")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;
    @Column
    private String name; // Название валюты
    @Column
    private String symbol; // Символ валюты, например BTC
    @Column
    private BigDecimal rate;
}
