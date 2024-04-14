package com.example.cryptoExchange.model;

import com.example.cryptoExchange.model.Transaction.Transaction;
import com.example.cryptoExchange.model.Wallet.CryptoWallet;
import com.example.cryptoExchange.model.Wallet.MoneyWallet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    //TODO cut to 2 wallets (changeset)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String username; // Имя пользователя
    @Column(nullable = false)
    private String dateOfBirth; // Дата рождения
    @Column(nullable = false)
    private String password; // Пароль
    @Column(nullable = false)
    private String email; // Электронная почта

    @Column
    private String name; // Имя
    @Column
    private String surname; // Фамилия
    @Column
    private String phoneNumber; // Телефон

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MoneyWallet> moneyWallet;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CryptoWallet> cryptoWallet;


    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;

    public String getRole() {
        return "USER";
    }
}