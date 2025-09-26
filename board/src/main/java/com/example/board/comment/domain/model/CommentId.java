package com.example.board.comment.domain.model;

import java.util.Objects;
import java.util.UUID;

public class CommentId {
    private final String value;

    private CommentId(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static CommentId generate() {
        return new CommentId(UUID.randomUUID().toString());
    }

    public static CommentId of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("CommentId cannot be null or empty");
        }
        return new CommentId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CommentId commentId = (CommentId) obj;
        return Objects.equals(value, commentId.value);
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
