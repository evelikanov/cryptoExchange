package com.example.cryptoExchange.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String evaluation;
    private String reviewText;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
