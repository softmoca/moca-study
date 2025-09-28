package com.example.board.user.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class RefreshToken {
    private final String value;
    private final LocalDateTime expiresAt;
    private final UserId userId;

    private RefreshToken(String value, LocalDateTime expiresAt, UserId userId) {
        this.value = Objects.requireNonNull(value, "Token value cannot be null");
        this.expiresAt = Objects.requireNonNull(expiresAt, "Expiration time cannot be null");
        this.userId = Objects.requireNonNull(userId, "User ID cannot be null");
    }

    public static RefreshToken of(String value, LocalDateTime expiresAt, UserId userId) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Token value cannot be null or empty");
        }
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token cannot be expired at creation");
        }
        return new RefreshToken(value.trim(), expiresAt, userId);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isValid() {
        return !isExpired();
    }

    public String getValue() {
        return value;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RefreshToken that = (RefreshToken) obj;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "value=***" +
                ", expiresAt=" + expiresAt +
                ", userId=" + userId +
                '}';
    }
}