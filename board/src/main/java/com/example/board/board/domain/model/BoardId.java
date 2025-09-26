package com.example.board.board.domain.model;

import java.util.Objects;
import java.util.UUID;

public class BoardId {
    private final String value;

    private BoardId(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static BoardId generate() {
        return new BoardId(UUID.randomUUID().toString());
    }

    public static BoardId of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("BoardId cannot be null or empty");
        }
        return new BoardId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BoardId boardId = (BoardId) obj;
        return Objects.equals(value, boardId.value);
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