package com.B2007186.AdviseNutrition.repository;

import com.B2007186.AdviseNutrition.domain.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface PostCatRepository extends JpaRepository<PostCategory, Long> {
    Optional<PostCategory> findByName(String name);
}
