// AccessToken.java - JWT 액세스 토큰 Value Object
package com.example.board.user.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class AccessToken {
    private final String value;
    private final LocalDateTime expiresAt;

    private AccessToken(String value, LocalDateTime expiresAt) {
        this.value = Objects.requireNonNull(value, "Token value cannot be null");
        this.expiresAt = Objects.requireNonNull(expiresAt, "Expiration time cannot be null");
    }

    public static AccessToken of(String value, LocalDateTime expiresAt) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Token value cannot be null or empty");
        }
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token cannot be expired at creation");
        }
        return new AccessToken(value.trim(), expiresAt);
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AccessToken that = (AccessToken) obj;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "value=***" +
                ", expiresAt=" + expiresAt +
                '}';
    }
}