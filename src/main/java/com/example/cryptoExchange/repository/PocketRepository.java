package com.example.cryptoExchange.repository;

import com.example.cryptoExchange.model.Pocket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PocketRepository extends JpaRepository<Pocket, Long> {
}