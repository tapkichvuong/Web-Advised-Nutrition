package com.B2007186.AdviseNutrition.service;


import com.B2007186.AdviseNutrition.domain.Product;

import com.B2007186.AdviseNutrition.domain.Users.Seller;
import com.B2007186.AdviseNutrition.domain.Users.User;

import com.B2007186.AdviseNutrition.dto.ProductReq;
import com.B2007186.AdviseNutrition.dto.ProductRes;
import com.B2007186.AdviseNutrition.exception.AuthorizationException;
import com.B2007186.AdviseNutrition.exception.ObjectNotFoundException;
import com.B2007186.AdviseNutrition.exception.ProductSoldOut;
import com.B2007186.AdviseNutrition.repository.ProductRepository;
import com.B2007186.AdviseNutrition.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final WishlistService wishlistService;
    public Product findById(Long id) {
        Optional<Product> obj = productRepository.findById(id);

        try {
            return obj.get();
        } catch (NoSuchElementException e) {
            throw new ObjectNotFoundException();
        }

    }
    private ProductRes mapToProductResponse(Product product) {
        return ProductRes.builder()
                .id(product.getId())
                .name(product.getName())
                .stockQuantity(product.getStockQuantity())
                .description(product.getDescription())
                .seller(product.getProductOwner().getFullName().toString())
                .build();
    }
    @Transactional
    public ProductRes insert(ProductReq obj) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(username).get();
        var product = Product.builder()
                .name(obj.getName())
                .description(obj.getDescription())
                .price(obj.getPrice())
                .stockQuantity(obj.getStockQuantity())
                .productOwner((Seller) user)
                .build();
        productRepository.save(product);
        return mapToProductResponse(product);
    }

    // verify if its product owner
    @Transactional
    public ProductRes update(ProductReq obj, Long productId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(username).get();
        Product product = findById(productId);

        if (!product.getProductOwner().equals(user)) {
            throw new AuthorizationException("You're not owner of this product");
        }
        if (Product.isSoldOut(findById(product.getId()))) {
            throw new ProductSoldOut();
        }
        product.setName(obj.getName());
        product.setDescription(obj.getDescription());
        product.setPrice(obj.getPrice());
        product.setStockQuantity(obj.getStockQuantity());
        productRepository.save(product);
        return mapToProductResponse(product);

    }

    public String delete(Long productId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(username).get();
        Product product = findById(productId);

        if (!product.getProductOwner().equals(user)) {
            throw new AuthorizationException("You're not owner of this product");
        }
        wishlistService.removeProductFromWishlistWhenIsDeleted(productId);
        productRepository.delete(product);
        return "Product has been deleted successfully";
    }

    public List<ProductRes> findAll() {

        List<Product> products = productRepository.findByStockQuantityGreaterThan(0);
        return products.stream().map(this::mapToProductResponse).toList();
    }

    public List<ProductRes> findOwnProducts() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller =(Seller) userRepository.findByUserName(username).get();
        List<Product> products = seller.getOwnProducts();
        return products.stream().map(this::mapToProductResponse).toList();
    }
}
