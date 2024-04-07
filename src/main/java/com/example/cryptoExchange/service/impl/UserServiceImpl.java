package com.example.cryptoExchange.service.impl;

import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.model.Wallet;
import com.example.cryptoExchange.repository.UserRepository;
import com.example.cryptoExchange.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public boolean authenticateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(u -> passwordEncoder.matches(password, u.getPassword())).orElse(false);
    }


    //Maybe add setPassword
    public User getDetailsByUsername(String username) {

        Optional<User> userOptional = userRepository.findByUsername(username);

        User user = userOptional.orElse(new User());
        user.getUsername();
        user.getDateOfBirth();
        user.getEmail();
        user.getId();
        return user;
    }

    //TODO add entity WALLET (changeset)
    public User getBalanceByUsername(String username) {

        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.get();
        Wallet wallet = new Wallet();
        wallet.setBalance(user.getBalance());
        wallet.setCurrency(user.getCurrency());
        return user;
    }

    //TODO add password changing (+ think about username setting)
    @Transactional
    public void updateUserDetails(Long id, String email, String dateOfBirth, String name) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setEmail(email);
        user.setDateOfBirth(dateOfBirth);
        user.setUsername(name);
        userRepository.updateUserDetails(id, email, dateOfBirth, name);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }
}