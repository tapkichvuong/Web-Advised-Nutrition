package com.B2007186.AdviseNutrition.domain;

import com.B2007186.AdviseNutrition.domain.Users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue
    @Column(name = "address_id",nullable = false, unique = true)
    private Long id;
    private String street;
    private String ward;
    private String city;
    private String country;
    private String zipcode;

    @OneToOne(mappedBy = "address", fetch = FetchType.LAZY)
    private User user;

    @OneToOne(mappedBy = "address", fetch = FetchType.LAZY)
    private Delivery delivery;
}
