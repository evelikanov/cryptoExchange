package com.example.cryptoExchange.dto;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.annotation.RequestScope;

import java.math.BigDecimal;
@RequestScope
@Getter
@Setter
public class CurrencyExchangeDTO {
    private String operationType;
    private String currencyToBuy;
    private String currencyToSell;
    private String username;
    @Nullable
    private BigDecimal amount;

}
