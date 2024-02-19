package com.B2007186.AdviseNutrition.domain.Users;

import com.B2007186.AdviseNutrition.domain.Order;
import com.B2007186.AdviseNutrition.domain.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("C")
@SuperBuilder(toBuilder = true)
public class Client extends User{
    @ManyToMany(mappedBy = "buyerOfTheProduct")
    private List<Product> boughtProducts;
    @ManyToMany(mappedBy = "wishesThisProduct")
    private Set<Product> productsWished;
    @OneToMany(mappedBy = "buyer")
    private List<Order> orders;
}
