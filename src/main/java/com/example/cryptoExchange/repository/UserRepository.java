package com.example.cryptoExchange.repository;

import com.example.cryptoExchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}