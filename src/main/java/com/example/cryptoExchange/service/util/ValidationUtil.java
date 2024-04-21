package com.example.cryptoExchange.service.util;

import com.example.cryptoExchange.constants.ErrorMessages;

import java.math.BigDecimal;

public class ValidationUtil{
    public static boolean isEmptyField(String... fields) {
        for(String field : fields) {
            if(field == null || field.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
    public static void validateNumber(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(ErrorMessages.NEGATIVE_NUMBER);
        }
    }
}
