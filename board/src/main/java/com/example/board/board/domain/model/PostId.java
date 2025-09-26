package com.example.board.board.domain.model;

import java.util.Objects;
import java.util.UUID;

public class PostId {
    private final String value;

    private PostId(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static PostId generate() {
        return new PostId(UUID.randomUUID().toString());
    }

    public static PostId of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("PostId cannot be null or empty");
        }
        return new PostId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PostId postId = (PostId) obj;
        return Objects.equals(value, postId.value);
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