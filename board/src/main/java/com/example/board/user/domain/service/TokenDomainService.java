// TokenDomainService.java - 토큰 관련 도메인 비즈니스 로직
package com.example.board.user.domain.service;

import com.example.board.user.domain.model.*;
import com.example.board.user.domain.repository.RefreshTokenRepository;
import com.example.board.user.domain.repository.UserRepository;
import com.example.board.common.exception.BusinessException;
import com.example.board.common.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenDomainService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public TokenPair authenticateAndGenerateTokens(User user) {
        // 기존 리프레시 토큰이 있다면 삭제 (한 사용자당 하나의 리프레시 토큰만 유지)
        refreshTokenRepository.deleteByUserId(user.getUserId());

        // 새로운 토큰 쌍 생성
        TokenPair tokenPair = tokenService.generateTokenPair(user);

        // 리프레시 토큰 저장
        refreshTokenRepository.save(tokenPair.getRefreshToken());

        return tokenPair;
    }

    public TokenPair refreshTokens(String refreshTokenValue) {
        // 리프레시 토큰 유효성 검증
        if (!tokenService.validateRefreshToken(refreshTokenValue)) {
            throw new BusinessException("유효하지 않은 리프레시 토큰입니다");
        }

        // 저장된 리프레시 토큰 조회
        RefreshToken storedRefreshToken = refreshTokenRepository.findByValue(refreshTokenValue)
                .orElseThrow(() -> new BusinessException("리프레시 토큰을 찾을 수 없습니다"));

        // 토큰 만료 확인
        if (storedRefreshToken.isExpired()) {
            refreshTokenRepository.deleteByValue(refreshTokenValue);
            throw new BusinessException("만료된 리프레시 토큰입니다");
        }

        // 사용자 조회
        User user = userRepository.findById(storedRefreshToken.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User", storedRefreshToken.getUserId().getValue()));

        // 사용자 활성 상태 확인
        if (!user.isActive()) {
            refreshTokenRepository.deleteByUserId(user.getUserId());
            throw new BusinessException("비활성화된 계정입니다");
        }

        // 기존 리프레시 토큰 삭제
        refreshTokenRepository.deleteByValue(refreshTokenValue);

        // 새로운 토큰 쌍 생성 및 저장
        return authenticateAndGenerateTokens(user);
    }

    public void revokeRefreshToken(String refreshTokenValue) {
        refreshTokenRepository.deleteByValue(refreshTokenValue);
    }

    public void revokeAllUserTokens(UserId userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    public boolean isAccessTokenValid(String accessTokenValue) {
        if (accessTokenValue == null || accessTokenValue.trim().isEmpty()) {
            return false;
        }

        return tokenService.validateAccessToken(accessTokenValue);
    }

    public UserId getUserIdFromAccessToken(String accessTokenValue) {
        if (!isAccessTokenValid(accessTokenValue)) {
            throw new BusinessException("유효하지 않은 액세스 토큰입니다");
        }

        return tokenService.extractUserIdFromAccessToken(accessTokenValue);
    }

    public User validateTokenAndGetUser(String accessTokenValue) {
        UserId userId = getUserIdFromAccessToken(accessTokenValue);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId.getValue()));

        if (!user.isActive()) {
            throw new BusinessException("비활성화된 계정입니다");
        }

        return user;
    }

    public void cleanupExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens();
    }
}