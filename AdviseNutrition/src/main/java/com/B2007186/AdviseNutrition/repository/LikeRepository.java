package com.B2007186.AdviseNutrition.repository;

import com.B2007186.AdviseNutrition.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndCommentId(Long userId, Long commentId);

    List<Like> findByCommentId(Long id);
}
