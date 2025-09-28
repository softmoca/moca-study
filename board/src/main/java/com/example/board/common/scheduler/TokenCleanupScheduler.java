package com.example.board.common.scheduler;

import com.example.board.user.domain.service.TokenDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final TokenDomainService tokenDomainService;

    // 매일 새벽 2시에 만료된 토큰들 정리
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredTokens() {
        try {
            log.info("Starting expired tokens cleanup...");
            tokenDomainService.cleanupExpiredTokens();
            log.info("Expired tokens cleanup completed");
        } catch (Exception e) {
            log.error("Error during expired tokens cleanup", e);
        }
    }
}
