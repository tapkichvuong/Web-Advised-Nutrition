package com.B2007186.AdviseNutrition.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorLicense {
    @Id
    @GeneratedValue
    @Column(name = "dl_id", nullable = false, unique = true)
    private Long id;
    private String specialize;
    private String range;
    private LocalDate licenseDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
