package com.example.cryptoExchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.cryptoExchange.repository")
@EntityScan("com.example.cryptoExchange.model")
public class CryptoExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoExchangeApplication.class, args);
	}

}
