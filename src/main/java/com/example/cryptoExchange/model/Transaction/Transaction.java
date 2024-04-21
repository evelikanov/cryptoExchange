package com.example.cryptoExchange.model.Transaction;

import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.model.Wallet.Wallet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "transaction")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet Wallet;

    @Column
    private LocalDateTime timestamp; // Время транзакции
    @Column
    private BigDecimal quantity; // Сумма транзакции
    @Column(name = "type", insertable = false, updatable = false)
    private String type; // Тип транзакции (например, покупка, продажа)
    @Column
    private String exchangeCurrency;
    @Column
    private BigDecimal exchangePrice;
    @Column
    private BigDecimal exchangeRate;

}