package com.example.board.user.application.dto;

import com.example.board.user.domain.model.User;
import com.example.board.user.domain.model.UserRole;
import java.time.LocalDateTime;

public record UserResponse(
        String userId,
        String email,
        String username,
        UserRole role,
        LocalDateTime createdAt,
        boolean active
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getUserId().getValue(),
                user.getEmail().getValue(),
                user.getUsername(),
                user.getRole(),
                user.getCreatedAt(),
                user.isActive()
        );
    }
}