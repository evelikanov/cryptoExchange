package com.example.cryptoExchange.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
@Getter
@Setter
public class UserDataDTO {
    private String username;
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private String dateOfBirth;
}
