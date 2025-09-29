package com.example.board.user.application.service;

import com.example.board.common.exception.BusinessException;
import com.example.board.user.application.dto.UserCreateRequest;
import com.example.board.user.application.dto.UserResponse;
import com.example.board.user.application.usecase.CreateUserUseCase;
import com.example.board.user.domain.User;
import com.example.board.user.repository.UserRepository;
import com.example.board.user.domain.service.UserDomainService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserApplicationService implements CreateUserUseCase {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        try {
            // 비밀번호 검증
            validatePassword(request.getPassword());

            // 도메인 서비스를 통한 유효성 검증
            userDomainService.validateUserForRegistration(
                    request.getUsername(),
                    request.getEmail()
            );

            // 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(request.getPassword());

            // 사용자 생성
            User user = new User(request.getEmail(), request.getUsername(), encodedPassword);

            // 저장
            User savedUser = userRepository.save(user);

            return UserResponse.from(savedUser);

        } catch (IllegalArgumentException e) {
            throw new BusinessException("사용자 생성 실패: " + e.getMessage());
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8 || password.length() > 50) {
            throw new IllegalArgumentException("Password must be between 8 and 50 characters");
        }
    }

    @Transactional(readOnly = true)
    public UserResponse findById(String userId) {
        try {
            Long id = Long.parseLong(userId);
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new BusinessException("사용자를 찾을 수 없습니다: " + userId));
            return UserResponse.from(user);
        } catch (NumberFormatException e) {
            throw new BusinessException("잘못된 사용자 ID 형식입니다");
        }
    }
}