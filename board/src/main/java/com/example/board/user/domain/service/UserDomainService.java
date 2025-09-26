package com.example.board.user.domain.service;

import com.example.board.user.domain.model.User;
import com.example.board.user.domain.model.Email;
import com.example.board.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;


    public boolean isEmailAvailable(Email email) {
        return !userRepository.existsByEmail(email);
    }

    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    public void validateUserForRegistration(String username, Email email) {
        if (!isEmailAvailable(email)) {
            throw new IllegalArgumentException("Email already exists: " + email.getValue());
        }

        if (!isUsernameAvailable(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }
    }
}