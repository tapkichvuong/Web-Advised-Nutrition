package com.B2007186.AdviseNutrition.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostReq {
    private String title;
    private String body;
    private String category;
    private MultipartFile thumbnail;
}
