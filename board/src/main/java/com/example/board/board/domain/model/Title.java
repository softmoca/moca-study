package com.example.board.board.domain.model;

import java.util.Objects;

public class Title {
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 200;

    private final String value;

    private Title(String value) {
        this.value = value;
    }

    public static Title of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        String trimmedValue = value.trim();
        if (trimmedValue.length() < MIN_LENGTH || trimmedValue.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Title must be between %d and %d characters", MIN_LENGTH, MAX_LENGTH));
        }

        return new Title(trimmedValue);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Title title = (Title) obj;
        return Objects.equals(value, title.value);
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