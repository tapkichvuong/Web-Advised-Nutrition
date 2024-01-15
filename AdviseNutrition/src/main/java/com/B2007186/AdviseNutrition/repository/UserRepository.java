package com.B2007186.AdviseNutrition.repository;

import com.B2007186.AdviseNutrition.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
}
