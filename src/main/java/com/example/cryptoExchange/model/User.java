package com.example.cryptoExchange.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

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

    public String getRole() {
        return "USER";
    }
//    @Column(nullable = false)
//    private String role;
}