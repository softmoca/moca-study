// 개선된 TokenDomainService.java - 순수 비즈니스 로직에 집중
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

    /**
     * 사용자 인증 후 토큰 쌍 생성
     * 로그인 시에만 사용됨
     */
    public TokenPair authenticateAndGenerateTokens(User user) {
        // 기존 리프레시 토큰이 있다면 삭제 (한 사용자당 하나의 리프레시 토큰만 유지)
        refreshTokenRepository.deleteByUserId(user.getUserId());

        // 새로운 토큰 쌍 생성
        TokenPair tokenPair = tokenService.generateTokenPair(user);

        // 리프레시 토큰 저장
        refreshTokenRepository.save(tokenPair.getRefreshToken());

        return tokenPair;
    }

    /**
     * 리프레시 토큰으로 새로운 토큰 쌍 생성
     */
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

        // 사용자 조회 (최신 정보 확인용)
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

    /**
     * 특정 리프레시 토큰 무효화 (로그아웃)
     */
    public void revokeRefreshToken(String refreshTokenValue) {
        refreshTokenRepository.deleteByValue(refreshTokenValue);
    }

    /**
     * 사용자의 모든 토큰 무효화 (전체 로그아웃)
     */
    public void revokeAllUserTokens(UserId userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    /**
     * 만료된 토큰들 정리 (스케줄러용)
     */
    public void cleanupExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens();
    }

    /**
     * 사용자 ID로 현재 사용자 정보 조회
     * 컨트롤러에서 최신 사용자 정보가 필요한 경우에만 사용
     */
    public User getUserById(UserId userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId.getValue()));
    }

    /**
     * 사용자 상태 변경 시 모든 토큰 무효화
     * 예: 사용자 비활성화, 권한 변경 등
     */
    public void invalidateTokensOnUserStatusChange(UserId userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}