package com.example.cryptoExchange.model.ReserveBank;


import com.example.cryptoExchange.model.ExchangeCurrency.Currency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Table(name = "money_reserve_bank")
@Entity
@Getter
@Setter
public class MoneyReserveBank extends ReserveBank {
    @Column
    private BigDecimal balance;
    @Column
    private String symbol;
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
}
