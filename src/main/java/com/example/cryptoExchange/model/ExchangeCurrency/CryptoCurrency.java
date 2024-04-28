package com.example.cryptoExchange.model.ExchangeCurrency;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "crypto_currency")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class CryptoCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;
    @Column
    private String name; // Название криптовалюты
    @Column
    private String symbol; // Символ криптовалюты, например BTC
    @Column
    private BigDecimal rate; // Текущая цена криптовалюты

    @Transient
    @JsonIgnore
    private transient String value;
    public CryptoCurrency(String value) {
        this.value = value;
    }
}