package com.B2007186.AdviseNutrition.controller;


import com.B2007186.AdviseNutrition.domain.Role;
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
import org.springframework.http.HttpStatus;
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
    private final static String  DEFAULT_AVATAR_PATH = "D:\\CTU\\NLNganh\\uploads\\defaultAvatar.png";
    private final AuthService service;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/register/client")
    public ResponseEntity<AuthenticationRes> registerCustomer(@RequestBody RegisterReq client)
            throws MessagingException, UnsupportedEncodingException {
        if (service.isUsernameTaken(client.getUserName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(AuthenticationRes.builder().message("Username already exists").build());
        }
        String randomCode = UUID.randomUUID().toString();
        Client user = Client.builder()
                .userName(client.getUserName())
                .email(client.getEmail())
                .avatar(DEFAULT_AVATAR_PATH)
                .passWord(passwordEncoder.encode(client.getPassword()))
                .role(Role.CLIENT)
                .verificationCode(randomCode)
                .postPermit(false)
                .enabled(false)
                .build();
        return ResponseEntity.ok(service.registerClient(user));
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<AuthenticationRes> registerDoctor(@RequestBody RegisterReq doctor)
            throws MessagingException, UnsupportedEncodingException {
        if (service.isUsernameTaken(doctor.getUserName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(AuthenticationRes.builder().message("Username already exists").build());
        }
        String randomCode = UUID.randomUUID().toString();
        Doctor user = Doctor.builder()
                .userName(doctor.getUserName())
                .email(doctor.getEmail())
                .avatar(DEFAULT_AVATAR_PATH)
                .passWord(passwordEncoder.encode(doctor.getPassword()))
                .role(Role.DOCTOR)
                .verificationCode(randomCode)
                .postPermit(false)
                .enabled(false)
                .build();
        return ResponseEntity.ok(service.registerDoctor(user));
    }
    @PostMapping("/register/seller")
    public ResponseEntity<AuthenticationRes> registerSeller(@RequestBody RegisterReq seller)
            throws MessagingException, UnsupportedEncodingException {
        if (service.isUsernameTaken(seller.getUserName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(AuthenticationRes.builder().message("Username already exists").build());
        }
        String randomCode = UUID.randomUUID().toString();
        Seller user = Seller.builder()
                .userName(seller.getUserName())
                .email(seller.getEmail())
                .avatar(DEFAULT_AVATAR_PATH)
                .passWord(passwordEncoder.encode(seller.getPassword()))
                .role(Role.SELLER)
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
