package com.example.board.user.domain.service;

import com.example.board.user.domain.model.*;
import com.example.board.user.domain.repository.UserRepository;
import com.example.board.common.exception.BusinessException;
import com.example.board.common.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenDomainService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    /**
     * 사용자 인증 후 액세스 토큰 생성
     * 로그인 시에만 사용됨
     */
    public AccessToken authenticateAndGenerateToken(User user) {
        // 사용자 활성 상태 확인
        if (!user.isActive()) {
            throw new BusinessException("비활성화된 계정입니다");
        }

        // 액세스 토큰 생성
        return tokenService.generateAccessToken(user);
    }

    /**
     * 사용자 ID로 현재 사용자 정보 조회
     * 컨트롤러에서 최신 사용자 정보가 필요한 경우에만 사용
     */
    public User getUserById(UserId userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId.getValue()));
    }
}