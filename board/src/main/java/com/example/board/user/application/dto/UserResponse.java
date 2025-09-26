package com.example.board.user.application.dto;

import com.example.board.user.domain.model.User;
import com.example.board.user.domain.model.UserRole;

import java.time.LocalDateTime;

public class UserResponse {
    private final String userId;
    private final String email;
    private final String username;
    private final UserRole role;
    private final LocalDateTime createdAt;
    private final boolean active;

    public UserResponse(String userId, String email, String username,
                        UserRole role, LocalDateTime createdAt, boolean active) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.role = role;
        this.createdAt = createdAt;
        this.active = active;
    }

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

    // Getter 메서드들
    public String getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public UserRole getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isActive() { return active; }
}