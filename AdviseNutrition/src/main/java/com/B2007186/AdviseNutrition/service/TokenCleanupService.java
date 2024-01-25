package com.B2007186.AdviseNutrition.service;

import com.B2007186.AdviseNutrition.domain.Token;
import com.B2007186.AdviseNutrition.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenCleanupService {
    private final TokenRepository tokenRepository; // Inject your TokenRepository here

    @Scheduled(fixedRate = 60 * 60 * 1000) // Run every 1 hour
    public void cleanupExpiredTokens() {
        System.out.println("Scheduled task running at: " + LocalDateTime.now());
        // Delete tokens older than a certain period
        List<Token> tokens = tokenRepository.findByRevocationDateBefore(LocalDateTime.now());
        tokenRepository.deleteAll(tokens);
    }
}
