package com.B2007186.AdviseNutrition.controller;

import com.B2007186.AdviseNutrition.dto.DrLicenseReq;
import com.B2007186.AdviseNutrition.dto.DrLicenseRes;
import com.B2007186.AdviseNutrition.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/doctor/license")
@RequiredArgsConstructor
public class LicenseController {
    private final LicenseService licenseService;
    @PostMapping
    public ResponseEntity<DrLicenseRes> addLicense(@RequestBody DrLicenseReq drLicenseReq){
        return ResponseEntity.ok(licenseService.addLicense(drLicenseReq));
    }

    @GetMapping
    public ResponseEntity<DrLicenseRes> getLicense(){
        return ResponseEntity.ok(licenseService.getLicense());
    }
    @PutMapping
    public ResponseEntity<DrLicenseRes> updateLicense(@RequestBody DrLicenseReq drLicenseReq){
        return ResponseEntity.ok(licenseService.updateLicense(drLicenseReq));
    }
    @DeleteMapping
    public ResponseEntity<String> deleteLicense(){
        return ResponseEntity.ok(licenseService.deleteLicense());
    }
}
