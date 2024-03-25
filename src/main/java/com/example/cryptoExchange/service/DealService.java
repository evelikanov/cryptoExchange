package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.Deal;
import java.util.List;

public interface DealService {
    Deal saveDeal(Deal deal);
    List<Deal> getAllDeals();
    Deal getDealById(Long id);
    void deleteDeal(Long id);
}