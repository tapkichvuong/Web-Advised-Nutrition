package com.B2007186.AdviseNutrition.repository;

import com.B2007186.AdviseNutrition.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);
    List<Post> findAllByOrderByCreatedAtDesc();
    @Query(value = """
      select t from Post t inner join t.user u\s
      where u.userName = :name\s
      """)
    Page<Post> findPostByUser(String name, Pageable pageable);;
}
