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
public class PostRes {
    private Long id;
    private String title;
    private String category;
    private String body;
    private String thumbnail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String firstName;
    private String lastName;
    private String avatar;
    private String message;
    private int statusCode;
}
