package com.example.board.user.domain.model;

import java.util.Objects;
import java.util.UUID;

public class UserId {
    private final String value;

    private UserId(String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID().toString());
    }

    public static UserId of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("UserId cannot be null or empty");
        }
        return new UserId(value);
    }

    public String getValue() {
        return value;
    }






    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;// 추가하자
        if (obj == null || getClass() != obj.getClass()) return false;
        UserId userId = (UserId) obj;
        return Objects.equals(value, userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value); //hashcode가 아니라 hash 쓰기 그래야 확장성이 좋음.
    }

    @Override
    public String toString() {
        return value;
    }
}