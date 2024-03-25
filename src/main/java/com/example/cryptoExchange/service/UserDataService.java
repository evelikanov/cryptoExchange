package com.example.cryptoExchange.service;

import com.example.cryptoExchange.model.UserData;
import java.util.List;

public interface UserDataService {
    UserData saveUserData(UserData userData);
    List<UserData> getAllUserData();
    UserData getUserDataById(Long id);
    void deleteUserData(Long id);
}