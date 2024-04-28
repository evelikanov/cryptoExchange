package com.example.cryptoExchange.dto;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.annotation.RequestScope;

import java.math.BigDecimal;

@RequestScope
@Getter
@Setter
public class CryptoCurrencyExchangeDTO {
    private String username;
    private String operationType;
    private String currency;
    private String cryptoCurrency;
    @Nullable
    private BigDecimal amount;
}
