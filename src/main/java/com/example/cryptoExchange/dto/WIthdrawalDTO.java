package com.example.cryptoExchange.dto;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.annotation.RequestScope;

import java.math.BigDecimal;


@RequestScope
@Getter
@Setter
public class WIthdrawalDTO {

    private String username;
    private String operationType;
    private String currency;
    @Nullable
    private BigDecimal balance;
    private String cryptoCurrency;
    @Nullable
    private BigDecimal amount;
}
