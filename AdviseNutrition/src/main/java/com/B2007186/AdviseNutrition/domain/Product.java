package com.B2007186.AdviseNutrition.domain;

import com.B2007186.AdviseNutrition.domain.Users.Client;
import com.B2007186.AdviseNutrition.domain.Users.Seller;
import com.B2007186.AdviseNutrition.exception.ProductSoldOut;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue
    @Column(name = "product_id", nullable = false, unique = true)
    private Long id;
    private String name;
    private Double price;
    private String description;
    private int stockQuantity;

    @ManyToMany(mappedBy = "products")
    private List<Category> categories;

    @ManyToOne
    private Seller productOwner;

    @ManyToMany
    @JoinTable(name = "CLIENT_PRODUCT", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Client> buyerOfTheProduct;

    @ManyToMany
    @JoinTable(name = "WISHLIST", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Client> wishesThisProduct;

    public static boolean isSoldOut(Product byId) {
        return byId.getStockQuantity() == 0;
    }

    /**
     * Increase Stock Quantity
     */
    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * Decrease Stock Quantity
     */
    public void decreaseStock(int quantity) {
        int restStock = this.stockQuantity - quantity;

        if(restStock < 0) {
            throw new ProductSoldOut("We need more stock.");
        }

        this.stockQuantity = restStock;
    }

    public Set<Client> getWhoWishesThisProduct() {
        return wishesThisProduct;
    }
}
