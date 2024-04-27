package com.B2007186.AdviseNutrition.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import com.B2007186.AdviseNutrition.domain.Token;
import com.B2007186.AdviseNutrition.domain.TokenType;
import com.B2007186.AdviseNutrition.domain.Users.Client;
import com.B2007186.AdviseNutrition.domain.Users.Doctor;
import com.B2007186.AdviseNutrition.domain.Users.Seller;
import com.B2007186.AdviseNutrition.domain.Users.User;
import com.B2007186.AdviseNutrition.dto.AuthenticationReq;
import com.B2007186.AdviseNutrition.dto.AuthenticationRes;
import com.B2007186.AdviseNutrition.repository.TokenRepository;
import com.B2007186.AdviseNutrition.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService{
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailSenderService emailSenderService;

    public boolean isUsernameTaken(String username) {
        return userRepository.findByUserName(username).isPresent();
    }

    public AuthenticationRes registerClient(Client user)
            throws MessagingException, UnsupportedEncodingException {
        Client savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        emailSenderService.sendVerificationEmail(user);
        return AuthenticationRes.builder()
                .role(user.getRole())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationRes registerDoctor(Doctor user)
            throws MessagingException, UnsupportedEncodingException {
        Doctor savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        emailSenderService.sendVerificationEmail(user);
        return AuthenticationRes.builder()
                .role(user.getRole())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    public AuthenticationRes registerSeller(Seller user)
            throws MessagingException, UnsupportedEncodingException {
        Seller savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        emailSenderService.sendVerificationEmail(user);
        return AuthenticationRes.builder()
                .role(user.getRole())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
    public AuthenticationRes authenticate(AuthenticationReq request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUserName(request.getUserName())
                .orElseThrow();
        if(!user.getEnabled()){
            return AuthenticationRes.builder()
                    .role(user.getRole())
                    .accessToken(null)
                    .refreshToken(null)
                    .message("Account is not available")
                    .build();
        }
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationRes.builder()
                .role(user.getRole())
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .message("Login successfully")
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .revocationDate(LocalDateTime.now().plusMinutes(60))
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUserName(refreshToken);
        if (username != null) {
            var user = userRepository.findByUserName(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationRes.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .username(user.getUsername())
                        .role(user.getRole())
                        .message("Refresh token successfully")
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode).get();

        if (user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);

            return true;
        }

    }
}

