package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User username);
    List<User> getAllUsers();
    User getUserById(Long id);
    void deleteUser(Long id);
}