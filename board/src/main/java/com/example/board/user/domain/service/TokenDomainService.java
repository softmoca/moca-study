package com.example.board.user.domain.service;

import com.example.board.user.domain.User;
import com.example.board.user.domain.AccessToken;
import com.example.board.user.repository.UserRepository;
import com.example.board.common.exception.BusinessException;
import com.example.board.common.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenDomainService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public AccessToken authenticateAndGenerateToken(User user) {
        if (!user.isActive()) {
            throw new BusinessException("비활성화된 계정입니다");
        }
        return tokenService.generateAccessToken(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId.toString()));
    }
}