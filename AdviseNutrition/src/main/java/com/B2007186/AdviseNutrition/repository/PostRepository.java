package com.B2007186.AdviseNutrition.repository;

import com.B2007186.AdviseNutrition.domain.DoctorLicense;
import com.B2007186.AdviseNutrition.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);
    List<Post> findAllByOrderByCreatedAtDesc();
}
