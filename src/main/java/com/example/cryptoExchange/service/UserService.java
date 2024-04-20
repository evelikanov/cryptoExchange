package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void createUser(String username, String password, String dateOfBirth, String email);
    Optional<User> findUserByUsername(String username);
    User saveUser(User username);
    List<User> getAllUsers();
    User getUserById(Long id);
    void deleteUser(Long id);
}