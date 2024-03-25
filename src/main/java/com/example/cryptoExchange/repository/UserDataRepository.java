package com.example.cryptoExchange.repository;

import com.example.cryptoExchange.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDataRepository extends JpaRepository<UserData, Long> {
}