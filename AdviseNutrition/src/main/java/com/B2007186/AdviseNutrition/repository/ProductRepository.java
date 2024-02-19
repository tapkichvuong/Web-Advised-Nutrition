package com.B2007186.AdviseNutrition.repository;

import com.B2007186.AdviseNutrition.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);
    @Modifying
    @Query(value="delete from wishlist where product_id = :productId and client_id = :clientId",nativeQuery = true)
    void removeFromClientWishlist(@Param("productId") Long productId, @Param("clientId") Long clientId);
    @Modifying
    @Query(value="delete from wishlist where product_id = :id",nativeQuery = true)
    void removeFromWishListWhenIsDeleted(@Param("id") Long id);
    List<Product> findByStockQuantityGreaterThan(Integer quantity);
}
