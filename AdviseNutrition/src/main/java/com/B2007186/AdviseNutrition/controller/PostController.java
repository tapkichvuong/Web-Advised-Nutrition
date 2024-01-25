package com.B2007186.AdviseNutrition.controller;

import com.B2007186.AdviseNutrition.domain.Post;
import com.B2007186.AdviseNutrition.dto.PostReq;
import com.B2007186.AdviseNutrition.dto.PostRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.B2007186.AdviseNutrition.service.PostService;

import java.util.List;

@RestController
@RequestMapping("api/v1/doctor/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostRes> addPost(@RequestBody PostReq post){
        return ResponseEntity.ok(postService.addPost(post));
    }
    @GetMapping("{postId}")
    public ResponseEntity<PostRes> getPost(@PathVariable("postId") Long postId){
        return ResponseEntity.ok(postService.getPost(postId));
    }
    @GetMapping
    public ResponseEntity<List<PostRes>> getPostList(){
        return ResponseEntity.ok(postService.getPostList());
    }

}
