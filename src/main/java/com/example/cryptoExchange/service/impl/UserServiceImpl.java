package com.example.cryptoExchange.service.impl;

import com.example.cryptoExchange.constants.ErrorMessages;
import com.example.cryptoExchange.dto.RegistrationDTO;
import com.example.cryptoExchange.model.User;
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

    //Registration
    public void validateRegistrationField(RegistrationDTO registrationDTO) {
        if(registrationDTO.getUsername().isEmpty() ||
                registrationDTO.getPassword().isEmpty() ||
                registrationDTO.getDateOfBirth().isEmpty() ||
                registrationDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.FIELDS_NOT_FILLED);
        }
    }
    public void validateUniqueUsername(RegistrationDTO registrationDTO) {
        if(userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new IllegalArgumentException(ErrorMessages.USERNAME_TAKEN);
        }
    }
    public void validateUniqueEmail(RegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new IllegalArgumentException(ErrorMessages.EMAIL_TAKEN);
        }
    }
    public void validateRegistration(RegistrationDTO registrationDTO) throws IllegalArgumentException {
        validateRegistrationField(registrationDTO);
        validateUniqueUsername(registrationDTO);
        validateUniqueEmail(registrationDTO);
    }
    public void createUser(String username, String password, String dateOfBirth, String email) {
        User user = new User();

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setDateOfBirth(dateOfBirth);
        userRepository.save(user);
    }
    public User getDetailsByUsername(String username) {

        Optional<User> userOptional = userRepository.findByUsername(username);

        User user = userOptional.orElse(new User());
        user.getUsername();
        user.getId();
        user.getName();
        user.getSurname();
        user.getPhoneNumber();
        user.getEmail();
        user.getDateOfBirth();
        return user;
    }
    @Transactional
    public String updateUserDetails(Long id, String name, String surname, String phoneNumber, String email, String dateOfBirth) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(ErrorMessages.USER_NOT_FOUND));

        if(!name.isEmpty()) {
            user.setName(name);
        }
        if(!surname.isEmpty()) {
            user.setSurname(surname);
        }
        if(!phoneNumber.isEmpty()) {
            user.setPhoneNumber(phoneNumber);
        }
        if(!email.isEmpty()) {
            user.setEmail(email);
        }
        if(!dateOfBirth.isEmpty()) {
            user.setDateOfBirth(dateOfBirth);
        }
        userRepository.updateUserDetails(id, name, surname, phoneNumber, email, dateOfBirth);
        return null;
    }

    public String isEmptySettingField(String name, String surname, String phoneNumber, String dateOfBirth, String email) {
        if(name.isEmpty() && surname.isEmpty() && phoneNumber.isEmpty() && dateOfBirth.isEmpty() && email.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.AT_LEAST_ONE_FIELD);
        }
        return null;
    }



    public String isExistedEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(ErrorMessages.EMAIL_TAKEN);
        }
        return null;
    }


    public String getRole() {
        return "USER";
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