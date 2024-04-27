package com.B2007186.AdviseNutrition.controller;

import com.B2007186.AdviseNutrition.domain.Post;
import com.B2007186.AdviseNutrition.dto.PostReq;
import com.B2007186.AdviseNutrition.dto.PostRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.B2007186.AdviseNutrition.service.PostService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/doctor/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostRes> addPost(PostReq post) throws IOException {
        return ResponseEntity.ok(postService.addPost(post));
    }

    @PutMapping("{postId}")
    public ResponseEntity<PostRes> updatePost(@PathVariable("postId") Long postId, PostReq post) throws IOException {
        PostRes response = postService.updatePost(postId, post);
        if (response.getStatusCode() == 404) {
            // Post not found
            return ResponseEntity.notFound().build();
        } else if (response.getStatusCode() == 401) {
            // Unauthorized user
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            // Successful update
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") Long postId){
        return ResponseEntity.ok(postService.deletePost(postId));
    }
}
