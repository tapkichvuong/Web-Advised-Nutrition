package com.B2007186.AdviseNutrition.domain.Users;

import com.B2007186.AdviseNutrition.domain.DoctorLicense;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("D")
@SuperBuilder(toBuilder = true)
public class Doctor extends User{
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "user")
    private DoctorLicense license;
}
