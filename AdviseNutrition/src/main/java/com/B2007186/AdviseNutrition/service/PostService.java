package com.B2007186.AdviseNutrition.service;

import com.B2007186.AdviseNutrition.domain.Post;
import com.B2007186.AdviseNutrition.dto.PostReq;
import com.B2007186.AdviseNutrition.dto.PostRes;
import com.B2007186.AdviseNutrition.repository.PostRepository;
import com.B2007186.AdviseNutrition.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public PostRes addPost(PostReq postReq) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUserName(username);
        if(!user.get().getIsActive()){
            return PostRes.builder().message("This account is not activated").build();
        }
        var post = Post.builder()
                .title(postReq.getTitle())
                .body(postReq.getBody())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user.get())
                .build();
        postRepository.save(post);
        return PostRes.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .message("Post has been added successfully")
                .build();
    }

    public PostRes getPost(long id) {
        var post = postRepository.findById(id).get();
        return PostRes.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .message("Fetch post successfully")
                .build();
    }

    private PostRes mapToTeachingResponse(Post post) {
        return PostRes.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .createdAt(post.getUpdatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
    public List<PostRes> getPostList() {
        try {
            List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();
            return postList.stream().map(this::mapToTeachingResponse).toList();
        } catch (Exception e) {
            // Handle JWT validation exception
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
