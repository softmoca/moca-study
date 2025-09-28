// RefreshTokenRepository.java - 리프레시 토큰 저장소 인터페이스
        package com.example.board.user.domain.repository;

import com.example.board.user.domain.model.RefreshToken;
import com.example.board.user.domain.model.UserId;

import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);
    Optional<RefreshToken> findByValue(String tokenValue);
    Optional<RefreshToken> findByUserId(UserId userId);
    void deleteByValue(String tokenValue);
    void deleteByUserId(UserId userId);
    void deleteExpiredTokens();
}