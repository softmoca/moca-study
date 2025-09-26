package com.example.board.user.application.service;

import com.example.board.common.exception.BusinessException;
import com.example.board.user.application.dto.LoginRequest;
import com.example.board.user.application.dto.LoginResponse;
import com.example.board.user.application.dto.UserResponse;
import com.example.board.user.application.usecase.AuthenticateUserUseCase;
import com.example.board.user.domain.model.Email;
import com.example.board.user.domain.model.User;
import com.example.board.user.domain.repository.UserRepository;
import com.example.board.common.util.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthenticationService implements AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // JWT 토큰 생성을 위한 서비스 (실제 구현에서는 JWT 라이브러리 사용)

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
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

            // JWT 토큰 생성 (실제로는 JWT 라이브러리 사용)
            String accessToken = generateAccessToken(user);

            return new LoginResponse(accessToken, UserResponse.from(user));

        } catch (IllegalArgumentException e) {
            throw new BusinessException("로그인 실패: " + e.getMessage());
        }
    }

    private String generateAccessToken(User user) {
        // 실제 구현에서는 JWT 라이브러리를 사용하여 토큰 생성
        // 여기서는 간단한 예시
        return "jwt-token-" + user.getUserId().getValue();
    }
}