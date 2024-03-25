package com.example.cryptoExchange.service.impl;

import com.example.cryptoExchange.model.UserData;
import com.example.cryptoExchange.repository.UserDataRepository;
import com.example.cryptoExchange.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDataServiceImpl implements UserDataService {

    private final UserDataRepository userDataRepository;

    @Autowired
    public UserDataServiceImpl(UserDataRepository userDataRepository) {
        this.userDataRepository = userDataRepository;
    }

    @Override
    public UserData saveUserData(UserData userData) {
        return userDataRepository.save(userData);
    }

    @Override
    public List<UserData> getAllUserData() {
        return userDataRepository.findAll();
    }

    @Override
    public UserData getUserDataById(Long id) {
        return userDataRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteUserData(Long id) {
        userDataRepository.deleteById(id);
    }
}