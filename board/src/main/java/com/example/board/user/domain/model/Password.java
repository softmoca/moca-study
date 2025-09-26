package com.example.board.user.domain.model;

import java.util.Objects;

public class Password {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 50;

    private final String value;
    private final boolean isEncoded;

    private Password(String value, boolean isEncoded) {
        this.value = value;
        this.isEncoded = isEncoded;
    }

    public static Password createRaw(String rawPassword) {
        if (rawPassword == null || rawPassword.length() < MIN_LENGTH || rawPassword.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Password must be between %d and %d characters", MIN_LENGTH, MAX_LENGTH));
        }
        return new Password(rawPassword, false);
    }

    public static Password createEncoded(String encodedPassword) {
        if (encodedPassword == null || encodedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Encoded password cannot be null or empty");
        }
        return new Password(encodedPassword, true);
    }

    public String getValue() {
        return value;
    }

    public boolean isEncoded() {
        return isEncoded;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Password password = (Password) obj;
        return Objects.equals(value, password.value) && isEncoded == password.isEncoded;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, isEncoded);
    }
}