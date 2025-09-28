package com.example.board.user.application.service;

import com.example.board.common.exception.BusinessException;
import com.example.board.user.application.dto.*;
import com.example.board.user.application.usecase.AuthenticateUserUseCase;
import com.example.board.user.domain.model.*;
import com.example.board.user.domain.repository.UserRepository;
import com.example.board.user.domain.service.TokenDomainService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final TokenDomainService tokenDomainService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public LoginResponse authenticate(LoginRequest request) {
        try {
            // 이메일로 사용자 조회
            Email email = Email.of(request.getEmail());
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessException("이메일 또는 비밀번호가 잘못되었습니다"));

            // 비밀번호 검증
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword().getValue())) {
                throw new BusinessException("이메일 또는 비밀번호가 잘못되었습니다");
            }

            // 활성화된 사용자인지 확인
            if (!user.isActive()) {
                throw new BusinessException("비활성화된 계정입니다");
            }

            // 토큰 쌍 생성
            TokenPair tokenPair = tokenDomainService.authenticateAndGenerateTokens(user);

            return LoginResponse.from(tokenPair, UserResponse.from(user));

        } catch (IllegalArgumentException e) {
            throw new BusinessException("로그인 실패: " + e.getMessage());
        }
    }

    @Transactional
    public RefreshTokenResponse refreshTokens(RefreshTokenRequest request) {
        try {
            TokenPair newTokenPair = tokenDomainService.refreshTokens(request.getRefreshToken());
            return RefreshTokenResponse.from(newTokenPair);
        } catch (Exception e) {
            throw new BusinessException("토큰 갱신 실패: " + e.getMessage());
        }
    }

    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken != null && !refreshToken.trim().isEmpty()) {
            tokenDomainService.revokeRefreshToken(refreshToken);
        }
    }

    @Transactional
    public void logoutAllDevices(String userId) {
        UserId userIdObj = UserId.of(userId);
        tokenDomainService.revokeAllUserTokens(userIdObj);
    }
}
