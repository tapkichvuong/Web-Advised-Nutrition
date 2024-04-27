package com.B2007186.AdviseNutrition.controller;

import com.B2007186.AdviseNutrition.dto.CommentRes;
import com.B2007186.AdviseNutrition.dto.PostRes;
import com.B2007186.AdviseNutrition.service.CommentService;
import com.B2007186.AdviseNutrition.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
public class NewsFeedController {
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("{postId}")
    public ResponseEntity<PostRes> getPost(@PathVariable("postId") Long postId) throws IOException {
        return ResponseEntity.ok(postService.getPost(postId));
    }
    @GetMapping
    public ResponseEntity<List<PostRes>> getPostList() throws IOException {
        return ResponseEntity.ok(postService.getAllPost());
    }

    @GetMapping("/mypost")
    public ResponseEntity<List<PostRes>> getMyPosts(@RequestParam(defaultValue = "0") int _start,
                                                    @RequestParam(defaultValue = "10") int _end,
                                                    @RequestParam(defaultValue = "id") String _sort,
                                                    @RequestParam(defaultValue = "ASC") String _order) throws IOException {
        List<PostRes> postResList = postService.getMyPosts(_start, _end, _sort, _order);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("X-Total-Count", String.valueOf(postResList.size()));
        responseHeaders.add("Access-Control-Expose-Headers", "X-Total-Count");
        return new ResponseEntity<>(postResList, responseHeaders, HttpStatus.OK);
    }
    @PostMapping("{postId}/comments")
    public ResponseEntity<CommentRes> addComment(@PathVariable("postId") Long postId, @RequestParam(name = "body") String body, @RequestParam(name = "parentId") Long parentId){
        if(body.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(commentService.addComment(postId, body, parentId));
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentRes> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestParam(name = "body") String body) {
        return ResponseEntity.ok(commentService.updateComment(commentId, body));
    }

    @PostMapping("{postId}/comments/{commentId}/toggleLike")
    public ResponseEntity<Map<String, String>> toggleLike(
            @PathVariable Long commentId) {

        if (SecurityContextHolder.getContext().getAuthentication().getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String result = commentService.toggleLike(commentId);
        Map<String, String> response = new HashMap<>();
        response.put("addLike", result);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Map<String, Long>> deleteComment(@PathVariable Long commentId) {
        Long result = commentService.deleteComment(commentId);
        if(result == 401){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Map<String, Long> response = new HashMap<>();
        response.put("id", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{postId}/comments")
    public ResponseEntity<List<CommentRes>> getComment(@PathVariable("postId") Long postId){
        return ResponseEntity.ok(commentService.getComment(postId));
    }
}
