package com.example.cryptoExchange.model.ReserveBank;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class ReserveBank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
