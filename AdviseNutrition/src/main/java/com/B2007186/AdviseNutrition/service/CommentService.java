package com.B2007186.AdviseNutrition.service;

import com.B2007186.AdviseNutrition.domain.Comment;
import com.B2007186.AdviseNutrition.domain.Like;
import com.B2007186.AdviseNutrition.dto.CommentRes;
import com.B2007186.AdviseNutrition.dto.PostRes;
import com.B2007186.AdviseNutrition.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final String FOLDER_PATH = "D:\\CTU\\NLNganh\\uploads\\";
    private final String BASE_IMAGE_URL = "http://localhost:8080/api/v1/image/";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    public CommentRes addComment(Long postId, String body, Long parent) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUserName(username).get();
        var post = postRepository.findById(postId).get();
        var parentCom = commentRepository.findById(parent);
        Comment comment;
        if(parentCom.isPresent()){
             comment = Comment.builder()
                    .body(body)
                    .post(post)
                    .user(user)
                    .parent(parentCom.get())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }else {
            comment = Comment.builder()
                    .body(body)
                    .post(post)
                    .user(user)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }

        commentRepository.save(comment);
        Long parentId = 0L;
        if(Objects.nonNull(comment.getParent())){
            parentId = comment.getParent().getId();
        }
        return CommentRes.builder()
                .id(comment.getId())
                .body(comment.getBody())
                .parentId(parentId)
                .userName(comment.getUser().getUsername())
                .firstName(comment.getUser().getFirstName())
                .lastName(comment.getUser().getLastName())
                .createdAt(comment.getCreatedAt())
                .statusCode(200)
                .message("Comment has been added successfully")
                .build();
    }

    public CommentRes updateComment(Long commentId, String body) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUserName(username).get();
        var comment = commentRepository.findById(commentId).get();
        if(!Objects.equals(user.getId(), comment.getUser().getId())){
            return CommentRes.builder().statusCode(401).build();
        }
        comment.setBody(body);
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        Long parentId = 0L;
        if(Objects.nonNull(comment.getParent())){
            parentId = comment.getParent().getId();
        }
        return CommentRes.builder()
                .id(comment.getId())
                .body(comment.getBody())
                .parentId(parentId)
                .userName(comment.getUser().getUsername())
                .firstName(comment.getUser().getFirstName())
                .lastName(comment.getUser().getLastName())
                .createdAt(comment.getCreatedAt())
                .statusCode(200)
                .message("Comment has been updated successfully")
                .build();
    }

    public String toggleLike (Long commentId){
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUserName(username).get();
        Long userId = user.getId();
        Optional<Like> like = likeRepository.findByUserIdAndCommentId(userId, commentId);
        if(like.isPresent()) {
            likeRepository.delete(like.get());
            return "false";
        }else {
            Comment comment = commentRepository.findById(commentId).get();
            Like saveLike =  Like.builder().user(user).comment(comment).build();
            likeRepository.save(saveLike);
            return "true";
        }
    }

    public Long deleteComment(Long commentId) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUserName(username).get();
        var comment = commentRepository.findById(commentId).get();
        if(!Objects.equals(user.getId(), comment.getUser().getId())){
            return 401L;
        }
        commentRepository.delete(comment);
        return commentId;
    }

    public List<CommentRes> getComment(Long postId) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();

        var post = postRepository.findById(postId).get();
        return post.getComments().stream()
                .map(comment -> convertComment(comment, username))
                .toList();
    }
    private CommentRes convertComment(Comment comment, String username) {
        List<Like> commentLikes = likeRepository.findByCommentId(comment.getId());
        var user = userRepository.findByUserName(username).get();
        Long userId = user.getId();
        boolean likedByMe = userId != null && commentLikes.stream().anyMatch(like -> like.getUser().getId().equals(userId));
        Long parentId = 0L;
        if(Objects.nonNull(comment.getParent())){
            parentId = comment.getParent().getId();
        }
        return CommentRes.builder()
                .id(comment.getId())
                .body(comment.getBody())
                .parentId(parentId)
                .createdAt(comment.getCreatedAt())
                .likeCount(commentLikes.size())
                .likedByMe(likedByMe)
                .userName(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .message("Fetch comment successfully")
                .statusCode(200)
                .build();
    }
}
