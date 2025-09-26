package com.example.board.user.infrastructure.entity;

public enum UserEntityRole {
    ADMIN("관리자"),
    USER("일반사용자");

    private final String description;

    UserEntityRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}