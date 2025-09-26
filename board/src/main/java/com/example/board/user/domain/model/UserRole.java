package com.example.board.user.domain.model;

public enum UserRole {
    ADMIN("관리자"),
    USER("일반사용자");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}