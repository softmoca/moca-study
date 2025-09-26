package com.example.board.user.application.service;

import com.example.board.common.exception.BusinessException;
import com.example.board.user.application.dto.UserCreateRequest;
import com.example.board.user.application.dto.UserResponse;
import com.example.board.user.application.usecase.CreateUserUseCase;
import com.example.board.user.domain.model.Email;
import com.example.board.user.domain.model.Password;
import com.example.board.user.domain.model.User;
import com.example.board.user.domain.model.UserId;
import com.example.board.user.domain.repository.UserRepository;
import com.example.board.user.domain.service.UserDomainService;
import com.example.board.common.util.PasswordEncoder;

import lombok.RequiredArgsConstructor;
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
            // 도메인 객체로 변환
            Email email = Email.of(request.getEmail());
            Password rawPassword = Password.createRaw(request.getPassword());

            // 도메인 서비스를 통한 유효성 검증
            userDomainService.validateUserForRegistration(request.getUsername(), email);

            // 비밀번호 암호화
            String encodedPasswordValue = passwordEncoder.encode(rawPassword.getValue());
            Password encodedPassword = Password.createEncoded(encodedPasswordValue);

            // 사용자 생성
            User user = new User(email, request.getUsername(), encodedPassword);

            // 저장
            User savedUser = userRepository.save(user);

            return UserResponse.from(savedUser);

        } catch (IllegalArgumentException e) {
            throw new BusinessException("사용자 생성 실패: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public UserResponse findById(String userId) {
        User user = userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> new BusinessException("사용자를 찾을 수 없습니다: " + userId));
        return UserResponse.from(user);
    }
}