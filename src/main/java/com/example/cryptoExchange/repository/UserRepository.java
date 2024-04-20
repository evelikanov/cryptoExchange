package com.example.cryptoExchange.repository;

import com.example.cryptoExchange.model.User;
import com.example.cryptoExchange.model.Wallet.MoneyWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void deleteByUsername(String username);
    @Modifying
    @Query("UPDATE User u SET " +
            "u.name = CASE WHEN :name IS NOT NULL AND :name <> '' THEN :name ELSE u.name END, " +
            "u.surname = CASE WHEN :surname IS NOT NULL AND :surname <> '' THEN :surname ELSE u.surname END, " +
            "u.phoneNumber = CASE WHEN :phoneNumber IS NOT NULL AND :phoneNumber <> '' THEN :phoneNumber ELSE u.phoneNumber END, " +
            "u.email = CASE WHEN :email IS NOT NULL AND :email <> '' THEN :email ELSE u.email END, " +
            "u.dateOfBirth = CASE WHEN :dateOfBirth IS NOT NULL AND :dateOfBirth <> '' THEN :dateOfBirth ELSE u.dateOfBirth END " +
            "WHERE u.id = :id")
    void updateUserDetails(@Param("id") Long id, @Param("name") String name, @Param("surname") String surname, @Param("phoneNumber") String phoneNumber, @Param("email") String email, @Param("dateOfBirth") String dateOfBirth);

}