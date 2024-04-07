package com.example.cryptoExchange.repository;

import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void deleteByUsername(String username);
    Wallet save(Wallet wallet);

    //TODO add password changing (+ think about username setting)
    @Modifying
    @Query("UPDATE User u SET u.email = :email, u.dateOfBirth = :dateOfBirth, u.username = :username WHERE u.id = :id")
    void updateUserDetails(@Param("id") Long id, @Param("email") String email, @Param("dateOfBirth") String dateOfBirth, @Param("username") String name);
    //CHECK
}