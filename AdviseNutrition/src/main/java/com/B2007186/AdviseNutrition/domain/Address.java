package com.B2007186.AdviseNutrition.domain;

import com.B2007186.AdviseNutrition.domain.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String district;
    private String province;

    @JsonIgnore
    @OneToOne(mappedBy = "address", fetch = FetchType.LAZY)
    private User user;
    @JsonIgnore
    @OneToOne(mappedBy = "address", fetch = FetchType.LAZY)
    private Delivery delivery;
}
