package com.B2007186.AdviseNutrition.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRes {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private int stockQuantity;
    private String seller;
}
