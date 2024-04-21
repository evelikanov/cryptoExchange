package com.example.cryptoExchange.config;

import com.example.cryptoExchange.model.ExchangeCurrency.CryptoCurrency;
import com.example.cryptoExchange.model.ReserveBank.CryptoReserveBank;
import com.example.cryptoExchange.model.ReserveBank.MoneyReserveBank;
import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.model.Wallet.CryptoWallet;
import com.example.cryptoExchange.model.Wallet.MoneyWallet;
import com.example.cryptoExchange.model.Wallet.Wallet;
import com.example.cryptoExchange.service.impl.ExchangeCurrencyServiceImpl.CryptoCurrencyServiceImpl;
import com.example.cryptoExchange.service.impl.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableAsync(proxyTargetClass = true)
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService();
    }
    @Bean
    public User user() {
        return new User();
    }
    @Bean
    public CryptoCurrency cryptoCurrency() {
        return new CryptoCurrency();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CryptoCurrencyServiceImpl cryptoCurrencyService() {
        return new CryptoCurrencyServiceImpl();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    //TODO turn on csrf
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/data", "/user/wallet", "/user/setting",
                                "/user/deals", "/user/wallet/topup", "/user/wallet/withdraw",
                                "/user/cryptoCurrencyList", "/user/buySellService", "/user/currencyExchangeService").authenticated()
                        .anyRequest().permitAll())
                .formLogin(form -> form.loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/home"))
                .authenticationProvider(authenticationProvider())
                .build();
    }
}