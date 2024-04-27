package com.B2007186.AdviseNutrition.repository;

import com.B2007186.AdviseNutrition.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
