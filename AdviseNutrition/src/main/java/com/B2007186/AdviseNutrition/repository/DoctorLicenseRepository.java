package com.B2007186.AdviseNutrition.repository;

import com.B2007186.AdviseNutrition.domain.DoctorLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DoctorLicenseRepository extends JpaRepository<DoctorLicense, Long> {
    Optional<DoctorLicense> findByUserId(Long id);

}
