package com.B2007186.AdviseNutrition.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRes {
    private Long id;
    private String body;
    private Long parentId;
    private LocalDateTime createdAt;
    private int likeCount;
    private boolean likedByMe;
    private String userName;
    private String firstName;
    private String lastName;
    private String message;
    private int statusCode;
}
