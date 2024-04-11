package com.example.cryptoExchange.model.Transaction;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("WITHDRAWAL")
public class WithdrawalTransaction extends Transaction {

}
