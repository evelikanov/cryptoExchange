package com.example.cryptoExchange.model.ExchangeCurrency;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "currency")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name; // Название валюты
    private String symbol; // Символ валюты, например BTC
}
