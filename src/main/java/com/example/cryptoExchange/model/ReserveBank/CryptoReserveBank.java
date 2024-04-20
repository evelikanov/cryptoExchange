package com.example.cryptoExchange.model.ReserveBank;

import com.example.cryptoExchange.model.ExchangeCurrency.CryptoCurrency;
import com.example.cryptoExchange.model.ExchangeCurrency.Currency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Table(name = "crypto_reserve_bank")
@Entity
@Getter
@Setter
public class CryptoReserveBank extends ReserveBank {
    @Column
    private BigDecimal amount;
    @Column
    private String symbol;
    @ManyToOne
    @JoinColumn(name = "crypto_currency_id")
    private CryptoCurrency cryptoCurrency;
}
