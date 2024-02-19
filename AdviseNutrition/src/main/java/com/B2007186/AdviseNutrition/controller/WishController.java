package com.B2007186.AdviseNutrition.controller;

import com.B2007186.AdviseNutrition.domain.Product;
import com.B2007186.AdviseNutrition.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/v1/wishlist")
@RequiredArgsConstructor
public class WishController {
    private final WishlistService service;
    @GetMapping
    public ResponseEntity<Set<Product>> returnWishlist() {

        return ResponseEntity.ok().body(service.findAll());
    }

    @PostMapping("/{productId}")
    public ResponseEntity<String> maskProductAsWished(@PathVariable Long productId) {
        return ResponseEntity.ok().body(service.markProductAsWished(productId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> delete(@PathVariable Long productId){
        return ResponseEntity.ok().body(service.delete(productId));
    }

}
