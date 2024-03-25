package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.Pocket;
import java.util.List;

public interface PocketService {
    Pocket savePocket(Pocket pocket);
    List<Pocket> getAllPockets();
    Pocket getPocketById(Long id);
    void deletePocket(Long id);
}