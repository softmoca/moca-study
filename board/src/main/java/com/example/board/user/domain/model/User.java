package com.example.board.user.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private final UserId userId;
    private final Email email;
    private final String username;
    private Password password;
    private UserRole role;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;

    // 생성자 - 새로운 사용자 생성
    public User(Email email, String username, Password password) {
        this.userId = UserId.generate();
        this.email = Objects.requireNonNull(email);
        this.username = validateUsername(username);
        this.password = Objects.requireNonNull(password);
        this.role = UserRole.USER;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.active = true;
    }

    // 생성자 - 기존 사용자 복원
    public User(UserId userId, Email email, String username, Password password,
                UserRole role, LocalDateTime createdAt, LocalDateTime updatedAt, boolean active) {
        this.userId = Objects.requireNonNull(userId);
        this.email = Objects.requireNonNull(email);
        this.username = validateUsername(username);
        this.password = Objects.requireNonNull(password);
        this.role = Objects.requireNonNull(role);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
        this.active = active;
    }

    private String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (username.length() < 3 || username.length() > 20) {
            throw new IllegalArgumentException("Username must be between 3 and 20 characters");
        }
        return username.trim();
    }

    // 비즈니스 메서드
    public void changePassword(Password newPassword) {
        this.password = Objects.requireNonNull(newPassword);
        this.updatedAt = LocalDateTime.now();
    }

    public void updateRole(UserRole newRole) {
        this.role = Objects.requireNonNull(newRole);
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isAdmin() {
        return UserRole.ADMIN.equals(this.role);
    }

    public boolean canWritePost() {
        return active;
    }

    public boolean canWriteComment() {
        return active;
    }

    // Getter 메서드들
    public UserId getUserId() { return userId; }
    public Email getEmail() { return email; }
    public String getUsername() { return username; }
    public Password getPassword() { return password; }
    public UserRole getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public boolean isActive() { return active; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}