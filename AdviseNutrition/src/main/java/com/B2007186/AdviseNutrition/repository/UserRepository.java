package com.B2007186.AdviseNutrition.repository;

import com.B2007186.AdviseNutrition.domain.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
@Repository

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUserName(String userName);

    @Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
    Optional<User> findByVerificationCode(String code);

    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
}
