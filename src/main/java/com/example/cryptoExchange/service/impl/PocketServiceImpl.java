package com.example.cryptoExchange.service.impl;

import com.example.cryptoExchange.model.Pocket;
import com.example.cryptoExchange.repository.PocketRepository;
import com.example.cryptoExchange.service.PocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PocketServiceImpl implements PocketService {

    private final PocketRepository pocketRepository;

    @Autowired
    public PocketServiceImpl(PocketRepository pocketRepository) {
        this.pocketRepository = pocketRepository;
    }

    @Override
    public Pocket savePocket(Pocket pocket) {
        return pocketRepository.save(pocket);
    }

    @Override
    public List<Pocket> getAllPockets() {
        return pocketRepository.findAll();
    }

    @Override
    public Pocket getPocketById(Long id) {
        return pocketRepository.findById(id).orElse(null);
    }

    @Override
    public void deletePocket(Long id) {
        pocketRepository.deleteById(id);
    }
}