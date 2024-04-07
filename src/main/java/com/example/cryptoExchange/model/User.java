package com.example.cryptoExchange.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    //TODO reset entity WALLET
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
    @Column(nullable = false)
    private BigDecimal balance; // Баланс денежных средств
    @Column(nullable = false)
    private BigDecimal currency; // Баланс криптовалютных средств

    public String getRole() {
        return "USER";
    }
}