package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void createUser(String username, String password, String dateOfBirth, String email);
    User getDetailsByUsername(String username);
    Optional<User> findUserByUsername(String username);
    User saveUser(User username);
    List<User> getAllUsers();
    User getUserById(Long id);
    void deleteAccountByUsername(String username);
    void validateUniqueUsername(String username);
    void validateFieldsNotEmpty(String... fields);
    void validateUniqueEmail(String email);
    void validateRegistration(String username, String password, String dateOfBirth, String email);
    void validateSettingFieldsNotEmpty(String name, String surname, String phoneNumber, String email, String dateOfBirth);
    void validateSettingChange(String name, String surname, String phoneNumber, String email, String dateOfBirth);
    void setUserDetails(String username, String name, String surname, String phoneNumber, String email, String dateOfBirth);
    void deleteUser(Long id);
}