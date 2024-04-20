package com.example.cryptoExchange.model.Transaction;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import static com.example.cryptoExchange.constants.TransactionMessages.TRANSACTION_TYPE_BUY;

@Entity
@Getter
@Setter
@DiscriminatorValue(TRANSACTION_TYPE_BUY)
public class BuyTransaction extends Transaction {

}
