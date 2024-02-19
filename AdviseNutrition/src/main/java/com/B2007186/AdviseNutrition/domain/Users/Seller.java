package com.B2007186.AdviseNutrition.domain.Users;

import com.B2007186.AdviseNutrition.domain.Order;
import com.B2007186.AdviseNutrition.domain.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("S")
@SuperBuilder(toBuilder = true)
public class Seller extends User{
    @OneToMany(mappedBy = "productOwner")
    private List<Product> ownProducts;
    @OneToMany(mappedBy = "seller")
    private List<Order> orders;
}
