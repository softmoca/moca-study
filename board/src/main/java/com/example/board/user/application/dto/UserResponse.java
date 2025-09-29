package com.example.board.user.application.dto;

import com.example.board.user.domain.User;
import com.example.board.user.domain.UserRole;
import java.time.LocalDateTime;

public record UserResponse(
        Long userId,           // String → Long 변경
        String email,
        String username,
        UserRole role,
        LocalDateTime createdAt,
        boolean active
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole(),
                user.getCreatedAt(),
                user.isActive()
        );
    }
}