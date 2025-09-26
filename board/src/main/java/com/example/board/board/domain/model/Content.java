package com.example.board.board.domain.model;

import java.util.Objects;

public class Content {
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 10000;

    private final String value;

    private Content(String value) {
        this.value = value;
    }

    public static Content of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        String trimmedValue = value.trim();
        if (trimmedValue.length() < MIN_LENGTH || trimmedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Content must be between %d and %d characters", MIN_LENGTH, MAX_LENGTH));
        }

        return new Content(trimmedValue);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Content content = (Content) obj;
        return Objects.equals(value, content.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
