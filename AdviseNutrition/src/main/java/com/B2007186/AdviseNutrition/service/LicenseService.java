package com.B2007186.AdviseNutrition.service;

import com.B2007186.AdviseNutrition.domain.DoctorLicense;
import com.B2007186.AdviseNutrition.dto.DrLicenseReq;
import com.B2007186.AdviseNutrition.dto.DrLicenseRes;
import com.B2007186.AdviseNutrition.repository.DoctorLicenseRepository;
import com.B2007186.AdviseNutrition.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LicenseService {
    private final DoctorLicenseRepository doctorLicenseRepository;
    private final UserRepository userRepository;
    public DrLicenseRes addLicense(DrLicenseReq drLicenseReq) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUserName(username);
        var license = DoctorLicense.builder()
                .specialize(drLicenseReq.getSpecialize())
                .range(drLicenseReq.getRange())
                .licenseDate(drLicenseReq.getLicenseDate())
                .user(user.get())
                .build();
        doctorLicenseRepository.save(license);
        user.get().setIsActive(true);
        userRepository.save(user.get());
        return DrLicenseRes
                .builder()
                .id(license.getId())
                .licenseDate(license.getLicenseDate())
                .specialize(license.getSpecialize())
                .range(license.getRange())
                .message("Add license sucessfully")
                .build();
    }
    public DrLicenseRes updateLicense(DrLicenseReq drLicenseReq) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUserName(username);
        var license = doctorLicenseRepository.findByUserId(user.get().getId()).get();
        license.setLicenseDate(license.getLicenseDate());
        license.setSpecialize(license.getSpecialize());
        license.setRange(license.getRange());
        doctorLicenseRepository.save(license);
        return DrLicenseRes
                .builder()
                .id(license.getId())
                .licenseDate(license.getLicenseDate())
                .specialize(license.getSpecialize())
                .range(license.getRange())
                .message("Update license sucessfully")
                .build();
    }

    public DrLicenseRes getLicense() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUserName(username);
        var license = doctorLicenseRepository.findByUserId(user.get().getId());
        return DrLicenseRes
                .builder()
                .id(license.get().getId())
                .licenseDate(license.get().getLicenseDate())
                .specialize(license.get().getSpecialize())
                .range(license.get().getRange())
                .message("Fetch license sucessfully")
                .build();
    }

    public String deleteLicense() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUserName(username);
        var license = doctorLicenseRepository.findByUserId(user.get().getId());
        doctorLicenseRepository.delete(license.get());
        user.get().setIsActive(false);
        userRepository.save(user.get());
        return "Delete successfully";
    }
}
