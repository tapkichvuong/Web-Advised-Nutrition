package com.B2007186.AdviseNutrition.controller;


import com.B2007186.AdviseNutrition.domain.Users.Client;
import com.B2007186.AdviseNutrition.domain.Users.Doctor;
import com.B2007186.AdviseNutrition.domain.Users.Seller;
import com.B2007186.AdviseNutrition.dto.AuthenticationReq;
import com.B2007186.AdviseNutrition.dto.AuthenticationRes;
import com.B2007186.AdviseNutrition.dto.RegisterReq;
import com.B2007186.AdviseNutrition.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/register/client")
    public ResponseEntity<AuthenticationRes> registerCustomer(@RequestBody RegisterReq client)
            throws MessagingException, UnsupportedEncodingException {
        String randomCode = UUID.randomUUID().toString();
        Client user = Client.builder()
                .userName(client.getUserName())
                .firstName(client.getFirstname())
                .lastName(client.getLastname())
                .email(client.getEmail())
                .passWord(passwordEncoder.encode(client.getPassword()))
                .role(client.getRole())
                .verificationCode(randomCode)
                .postPermit(false)
                .enabled(false)
                .build();
        return ResponseEntity.ok(service.registerClient(user));
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<AuthenticationRes> registerDoctor(@RequestBody RegisterReq doctor)
            throws MessagingException, UnsupportedEncodingException {
        String randomCode = UUID.randomUUID().toString();
        Doctor user = Doctor.builder()
                .userName(doctor.getUserName())
                .firstName(doctor.getFirstname())
                .lastName(doctor.getLastname())
                .email(doctor.getEmail())
                .passWord(passwordEncoder.encode(doctor.getPassword()))
                .role(doctor.getRole())
                .verificationCode(randomCode)
                .postPermit(false)
                .enabled(false)
                .build();
        return ResponseEntity.ok(service.registerDoctor(user));
    }
    @PostMapping("/register/seller")
    public ResponseEntity<AuthenticationRes> registerSeller(@RequestBody RegisterReq seller)
            throws MessagingException, UnsupportedEncodingException {
        String randomCode = UUID.randomUUID().toString();
        Seller user = Seller.builder()
                .userName(seller.getUserName())
                .firstName(seller.getFirstname())
                .lastName(seller.getLastname())
                .email(seller.getEmail())
                .passWord(passwordEncoder.encode(seller.getPassword()))
                .role(seller.getRole())
                .verificationCode(randomCode)
                .postPermit(false)
                .enabled(false)
                .build();
        return ResponseEntity.ok(service.registerSeller(user));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationRes> authenticate(@RequestBody AuthenticationReq auth) {
        return ResponseEntity.ok(service.authenticate(auth));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (service.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }
}
