package com.B2007186.AdviseNutrition.controller;

import com.B2007186.AdviseNutrition.dto.ProductReq;
import com.B2007186.AdviseNutrition.dto.ProductRes;
import com.B2007186.AdviseNutrition.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping("/{id}")
    public ResponseEntity<ProductRes> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(productService.getProduct(id));
    }

    @GetMapping

    public ResponseEntity<List<ProductRes>> findAll() {

        List<ProductRes> products = productService.findAll();
        return ResponseEntity.ok().body(products);
    }


    @GetMapping("/own")

    public ResponseEntity<List<ProductRes>> findOwnProducts() {

        List<ProductRes> products = productService.findOwnProducts();
        return ResponseEntity.ok().body(products);
    }


    @PostMapping("/new")
    public ResponseEntity<ProductRes> insert(@RequestBody ProductReq obj) {
        return ResponseEntity.ok().body(productService.insert(obj));
    }


    @PutMapping("{productId}/edit")
    public ResponseEntity<ProductRes> update(@RequestBody ProductReq obj,
                                          @PathVariable Long productId) {
        return ResponseEntity.ok().body(productService.update(obj, productId));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        ;
        return ResponseEntity.ok().body(productService.delete(id));

    }

}
