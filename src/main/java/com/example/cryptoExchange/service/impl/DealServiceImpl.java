package com.example.cryptoExchange.service.impl;

import com.example.cryptoExchange.model.Deal;
import com.example.cryptoExchange.repository.DealRepository;
import com.example.cryptoExchange.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;

    @Autowired
    public DealServiceImpl(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    @Override
    public Deal saveDeal(Deal deal) {
        return dealRepository.save(deal);
    }

    @Override
    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal getDealById(Long id) {
        return dealRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteDeal(Long id) {
        dealRepository.deleteById(id);
    }
}