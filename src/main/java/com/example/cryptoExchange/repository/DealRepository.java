package com.example.cryptoExchange.repository;

import com.example.cryptoExchange.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {
}