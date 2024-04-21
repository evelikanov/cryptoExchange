package com.example.cryptoExchange.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {
    private String username;
    private String password;
    private String dateOfBirth;
    private String email;
}
