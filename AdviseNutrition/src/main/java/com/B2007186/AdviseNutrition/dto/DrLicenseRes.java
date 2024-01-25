package com.B2007186.AdviseNutrition.dto;

import com.B2007186.AdviseNutrition.domain.DoctorLicense;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DrLicenseRes {
    private Long id;
    private String specialize;
    private String range;
    private LocalDate licenseDate;
    private String message;
}
