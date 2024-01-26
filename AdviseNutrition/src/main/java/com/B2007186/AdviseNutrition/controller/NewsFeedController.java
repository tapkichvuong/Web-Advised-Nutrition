package com.B2007186.AdviseNutrition.controller;

import com.B2007186.AdviseNutrition.dto.PostRes;
import com.B2007186.AdviseNutrition.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/v1/post")
@RequiredArgsConstructor
public class NewsFeedController {
    private final PostService postService;

    @GetMapping("{postId}")
    public ResponseEntity<PostRes> getPost(@PathVariable("postId") Long postId){
        return ResponseEntity.ok(postService.getPost(postId));
    }
    @GetMapping
    public ResponseEntity<List<PostRes>> getPostList(){
        return ResponseEntity.ok(postService.getPostList());
    }

    @PostMapping("{postId}/comment")
    public ResponseEntity<PostRes> addComment(@PathVariable("postId") Long postId, @RequestParam(name = "body") String body){
        return ResponseEntity.ok(postService.addComment(postId, body));
    }
}
