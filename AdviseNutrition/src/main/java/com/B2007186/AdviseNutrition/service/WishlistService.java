package com.B2007186.AdviseNutrition.service;

import com.B2007186.AdviseNutrition.domain.Product;
import com.B2007186.AdviseNutrition.domain.Users.Client;
import com.B2007186.AdviseNutrition.exception.ProductSoldOut;
import com.B2007186.AdviseNutrition.repository.ProductRepository;
import com.B2007186.AdviseNutrition.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private Client findByUserName() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return (Client) userRepository.findByUserName(username).get();
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id).get();
    }

    public Set<Product> findAll() {
        Client cli = findByUserName();

        return cli.getProductsWished();

    }

    public String markProductAsWished(Long productId) {
        Product product = findProductById(productId);
        Client client = findByUserName();

        if (Product.isSoldOut(product)) {
            throw new ProductSoldOut();
        }

        client.getProductsWished().add(product);
        product.getWhoWishesThisProduct().add(client);

        userRepository.save(client);
        productRepository.save(product);
        return "Set marker successfully";
    }

    @Transactional
    public String delete(Long productId) {
        Client user = findByUserName();

        productRepository.removeFromClientWishlist(productId, user.getId());
        return "Wish products has been removed successfully";
    }

    @Transactional
    public void removeProductFromWishlistWhenIsDeleted(Long productId) {
        productRepository.removeFromWishListWhenIsDeleted(productId);
    }
}
